package org.liamjd.bascule.lib.model

import com.vladsch.flexmark.util.data.MutableDataSet
import org.yaml.snakeyaml.Yaml
import java.io.File
import kotlin.collections.filterNot

typealias Theme = String
typealias YamlConfig = String

const val DEFAULT_GEN_PACKAGE = "org.liamjd.bascule.pipeline."

/**
 * Class representing the overall structure of the project, mostly the directory locations for source files, templates, etc
 */

class Project(yamlString: YamlConfig) {

    val name: String // this is probably unnecessary but harmless
    val theme: Theme
    val tags: Set<String>
    val dateFormat: String
    val dateTimeFormat: String
    val config: ProjectConfig
    val customAttributes: Map<String, Any>
    val postsPerPage: Int
    var clean: Boolean = true

    val model: Map<String, Any>
        get() = ProjectProperties(name, theme, dateFormat, dateTimeFormat, tags, postsPerPage).toMap() + customAttributes

    init {
        // Split the YAML config into separate documents
        val yaml = Yaml();
        if (yamlString.isBlank()) {
            throw RuntimeException("Yaml configuration file is blank!")
        }
        val yamlDocuments = splitYamlDocuments(yamlString)

        if (yamlDocuments.size < 2) {
            throw RuntimeException("Yaml configuration file is missing required fields - there there must be at least two documents")
        }
        if (yamlDocuments.size > 3) {
            throw RuntimeException("Yaml configuration file has too many documents - there there must be at most three documents")
        }

        val parentFolder = File(System.getProperty("user.dir"))


        // Parse document 0, the mandatory project properties
        if (yamlDocuments[0].isBlank()) throw RuntimeException("Must specify at least a name and a theme for the project")

        val projectProperties: Map<String, Any> = yaml.load(yamlDocuments[0])
        name = getConfigString(projectProperties, "name", parentFolder.name)
        theme = getConfigString(projectProperties, "theme")
        dateFormat = getConfigString(projectProperties, "dateFormat", "YYYY-MM-dd")
        dateTimeFormat = getConfigString(projectProperties, "dateTimeFormat", "YYYY-MM-dd HH:mm:ss")
        tags = getConfigSet(projectProperties, "tags")
        postsPerPage = getConfigInt(projectProperties, "postsPerPage")

        // Parse document 1, the mandatory project configuration - there are sensible but unuseful defaults for this, so it is not strictly required, but it is good practice to include it
        config = ProjectConfig(yamlDocuments[1], parentFolder)

        // Parse document 2, the optional custom attributes
        customAttributes = if (yamlDocuments.size == 3) {
            yaml.load(yamlDocuments[2]) as Map<String, Any>
        } else {
            emptyMap()
        }
    }

    /**
     * Splits a YAML string into separate documents based on the YAML document separator "---"
     * There three types of YAML documents:
     * 1. The first is the project properties
     * 2. The second is the project configuration
     * 3. The third are any custom attributes
     */
    private fun splitYamlDocuments(yamlConfig: String) = yamlConfig.split(
        Regex("---")
    ).filter(
        String::isNotBlank
    ).toTypedArray()

    /**
     * Finds the value of a key in the config map, if it exists, otherwise throws an exception
     */
    private fun getConfigString(map: Map<String, Any>, keyName: String) =
        if (map[keyName] == null) throw RuntimeException("Missing required key '$keyName' in configuration") else map[keyName] as String

    /**
     * Finds the value of a key in the config map, if it exists, otherwise returns the default value
     */
    private fun getConfigString(map: Map<String, Any>, keyName: String, defaultValue: String) =
        if (map[keyName] == null) defaultValue else map[keyName] as String

    /**
     * Finds the value of a key in the config map, and return it as an Int, if it exists, otherwise returns 1
     */
    private fun getConfigInt(map: Map<String, Any>, keyName: String) =
        if (map[keyName] == null) 1 else map[keyName] as Int

    /**
     * Finds the  value of a key in the config map, and return it as a Set, if it exists, otherwise returns an empty set
     */
    private fun getConfigSet(map: Map<String, Any>, keyName: String) =
        if (map[keyName] == null) emptySet() else map[keyName] as Set<String>

}

/**
 * The ProjectConfig contains the project configuration properties, such as directories, extensions, and generators, that are not part of the render model
 * There are sensible, if not very useful, defaults, so an empty string is valid
 */
class ProjectConfig(configuration: String, parentFolder: File) {
    val directories: Directories
    val extensions: ArrayList<String>?
    val generators: ArrayList<String>?
    var postLayouts: Set<String>
    val parentDir: File = parentFolder

    val markdownOptions: MutableDataSet = MutableDataSet()

    init {
        val yaml = Yaml()
        if (configuration.isBlank()) {
            directories = Directories.Defaults.get(parentFolder)
            extensions = null
            generators = null
            postLayouts = setOf("post")
        } else {
            val configMap: Map<String, Any> = yaml.load(configuration)
            directories = getConfigDirectories(configMap, parentFolder)
            extensions = getConfigPlugins(configMap, "extensions")
            generators = getConfigPlugins(configMap, "generators")
            postLayouts = getPostLayoutConfig(configMap)
        }
    }

    /**
     * Build the Directories object from the configuration map; if the directories section is not present, throw and exception
     */
    private fun getConfigDirectories(configMap: Map<String, Any>, parentFolder: File): Directories {
        if (configMap.isEmpty()) throw IllegalArgumentException("Invalid configuration: configuration map is empty")
        if (!configMap.containsKey("directories")) {
            throw IllegalArgumentException("Invalid configuration: directories section is missing")
        } else {
            @Suppress("UNCHECKED_CAST")
            val dirs = configMap["directories"] as Map<String, Any>
            val sourceDir = getDirectory(dirs, "source", "sources", parentFolder)
            val assetsDir = getDirectory(dirs, "assets", "assets", parentFolder)
            val templatesDir = getDirectory(dirs, "templates", "templates", parentFolder)
            val outputDir = getDirectory(dirs, "output", "output", parentFolder)

            val custom = dirs["custom"]
            var customDirs: MutableMap<String, File>? = mutableMapOf<String, File>()
            if (custom != null) {
                @Suppress("UNCHECKED_CAST")
                for (customKey in custom as Map<String, String>) {
                    @Suppress("UNCHECKED_CAST")
                    customDirs?.put(customKey.key, File(parentFolder, custom[customKey.key]))
                }
            }
            if (customDirs != null && customDirs.isEmpty()) {
                customDirs = null
            }

            return Directories(parentFolder, sourceDir, outputDir, assetsDir, templatesDir, customDirs)
        }
    }

    /**
     * Get a specific named directory from the configuration map, if it exists, otherwise return the default directory
     */
    private fun getDirectory(dirMap: Map<String, Any>, dirName: String, defaultName: String, parentFolder: File): File {
        val foundFolderName = if (dirMap[dirName] == null) {
            defaultName
        } else {
            dirMap[dirName]!!
        }
        return File(parentFolder, foundFolderName as String)
    }

    /**
     * Get the list of extensions from the configuration map, if it exists, otherwise return null
     */
    private fun getConfigPlugins(configMap: Map<String, Any?>, pluginName: String): ArrayList<String>? {
        if (configMap[pluginName] != null) {
            @Suppress("UNCHECKED_CAST")
            val pluginArray = configMap[pluginName] as ArrayList<String>
            var packagedArray = mutableListOf<String>()

            for (plugin in pluginArray) {
                if (!plugin.contains(".")) {
                    packagedArray.add(DEFAULT_GEN_PACKAGE + plugin)
                } else {
                    packagedArray.add(plugin)
                }
            }
            return ArrayList(packagedArray)

        }
        return null
    }

    /**
     * Get the custom post layouts from the configuration map, if it exists, otherwise return the default layout 'post'
     */
    private fun getPostLayoutConfig(configMap: Map<String, Any>): Set<String> {
        if (configMap["postLayouts"] == null) {
            return setOf("post")
        } else {
            val layoutList = configMap["postLayouts"] as ArrayList<String>
            return layoutList.toSet()
        }
    }
}

/**
 * Helper class to represent mandatory project properties
 */
internal class ProjectProperties(
    val name: String,
    val theme: String,
    val dateFormat: String = "YYYY-MM-dd",
    val dateTimeFormat: String = "YYYY-MM-dd HH:mm:ss",
    val tags: Set<String> = emptySet(),
    val postsPerPage: Int = 5
) {
    fun toMap(): Map<String, Any> = mapOf(
        "name" to name,
        "theme" to theme,
        "dateFormat" to dateFormat,
        "dateTimeFormat" to dateTimeFormat,
        "tags" to tags,
        "postsPerPage" to postsPerPage
    )
}