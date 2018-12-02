package org.liamjd.bascule.lib.model

import java.io.File
import java.io.InputStream

typealias Theme = String

// TODO: probably too much in this constructor... allow some vars!
// TODO: there's stuff in the model which shouldn't be there, like the directory names
/**
 * Class representing the overall structure of the project, mostly the directory locations for source files, templates, etc
 */
class Project(val name: String, val root: File, val sourceDir: File, val outputDir: File, val assetsDir: File, val templatesDir: File, val yamlConfigString: String, val theme: Theme, val model: Map<String, Any>) {

    var postsPerPage = 5
    var yamlMap: Map<String,Any> = mutableMapOf()

    constructor(name: String, root: File, source: String, output: String, assets: String, templates: String, yaml: String, themeName: String, configMap: Map<String, Any>) : this(name = name,
        root = root,
        sourceDir = File(root, source),
        outputDir = File(root, output),
        assetsDir = File(root, assets),
        templatesDir = File(root, templates),
        yamlConfigString = yaml,
        theme = themeName,
        model = configMap)

    constructor(name: String, root: File, sourceDir: File, outputDir: File, assetsDir: File, templatesDir: File, yamlConfigString: String, theme: Theme) : this(name = name,
        root = root,
        sourceDir = sourceDir,
        outputDir = outputDir,
        assetsDir = assetsDir,
        templatesDir = templatesDir,
        yamlConfigString = yamlConfigString,
        theme = theme,
        model = mapOf())


    override fun toString(): String {
        return "ProjectStructure: name: $name, root: $root\n" +
                "source: $sourceDir, output: $outputDir, assets: $assetsDir, templates: $templatesDir\n" +
                "theme: $theme\n" +
                "model: $model"

    }

}

