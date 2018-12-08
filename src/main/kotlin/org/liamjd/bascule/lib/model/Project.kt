package org.liamjd.bascule.lib.model

import org.yaml.snakeyaml.Yaml
import java.io.File

typealias Theme = String
typealias YamlConfig = String

/**
 * Class representing the overall structure of the project, mostly the directory locations for source files, templates, etc
 */

class Project(yamlConfig: YamlConfig) {

	val name: String
	val theme: Theme
	val postsPerPage: Int
	val configMap: Map<String, Any>
	val model: Map<String, Any>
	val dirs: Directories
	val generators: ArrayList<String>?
	val parentFolder: File

	init {

		// initial setup
		val yaml = Yaml();
		if (yamlConfig.isBlank()) {
			throw RuntimeException("Yaml configuration file is blank!")
		}
		configMap = yaml.load(yamlConfig)
		parentFolder = File(System.getProperty("user.dir"))

		// set values
		name = parentFolder.name
		theme = getConfigString("theme", "bulma")
		postsPerPage = getConfigInt("postsPerPage")
		dirs = getConfigDirectories()

		val tempModel = mutableMapOf<String, Any>()
		tempModel.putAll(configMap)
		model = tempModel

		generators = getConfigGenerators()
	}

	override fun toString(): String {
		return "ProjectStructure: name: $name,\n" +
				"theme: $theme\n" +
				dirs +
				"model: $model"

	}

	// local functions
	private fun getConfigString(keyName: String, defaultValue: String) = if (configMap[keyName] == null) defaultValue else configMap[keyName] as String

	fun getConfigString(map: Map<String, String>, keyName: String, defaultValue: String): String {
		if (map[keyName] == null) return defaultValue else {
			return configMap[keyName] as String
		}
	}

	private fun getConfigInt(keyName: String) = if (configMap[keyName] == null) 1 else configMap[keyName] as Int

	private fun getDirectory(dirMap: Map<String, Any>, dirName: String, defaultName: String): File {
		val foundFolderName = if (dirMap[dirName] == null) {
			defaultName
		} else {
			dirMap[dirName]!!
		}
		return File(parentFolder, foundFolderName as String)
	}

	@Suppress("UNCHECKED_CAST")
	private fun getConfigDirectories(): Directories {
		if (configMap["directories"] != null) {

			val dirs = configMap["directories"] as Map<String, Any>
			val sourceDir = getDirectory(dirs, "source", "sources")
			val assetsDir = getDirectory(dirs, "assets", "assets")
			val templatesDir = getDirectory(dirs, "templates", "templates")
			val outputDir = getDirectory(dirs, "output", "output")

			val custom = dirs["custom"]
			var customDirs: MutableMap<String, File>? = mutableMapOf<String, File>()
			if (custom != null) {
				for (customKey in custom as Map<String, String>) {
					customDirs?.put(customKey.key, File(parentFolder, custom[customKey.key]))
				}
			}
			if (customDirs != null && customDirs.isEmpty()) {
				customDirs = null
			}

			return Directories(parentFolder, sourceDir, outputDir, assetsDir, templatesDir, customDirs)
		} else return Directories.Defaults.get(parentFolder)

	}

	private fun getConfigGenerators() : ArrayList<String>? {
		if(configMap["generators"] != null) {
			return configMap["generators"] as ArrayList<String>
		}
		return null
	}
}
