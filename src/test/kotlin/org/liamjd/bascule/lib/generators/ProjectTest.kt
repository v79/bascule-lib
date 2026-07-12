package org.liamjd.bascule.lib.generators

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.liamjd.bascule.lib.model.Project

class ProjectTest {

    @Test
    fun `throws exception for an empty file`() {
        assertThrows<RuntimeException> {
            Project(yaml.EMPTY)
        }
    }

    @Test
    fun `constructs project with the minimal yaml file`() {
        val project = Project(yaml.MINIMAL)
        assertNotNull(project)
        assertEquals(Expected.name, project.name)
        assertEquals(Expected.theme, project.theme)
    }

    @Test
    fun `throws exception for an invalid directory`() {
        assertThrows<RuntimeException> {
            Project(yaml.INVALID_DIRECTORIES)
        }
    }

    @Test
    fun `minimal yaml file has default directories defined`() {
        val project = Project(yaml.MINIMAL)

        assertNotNull(project)
        assertEquals(Expected.sources, project.config.directories.sources.name)
        assertEquals(Expected.output, project.config.directories.output.name)
        assertEquals(Expected.templates, project.config.directories.templates.name)
        assertEquals(Expected.assets, project.config.directories.assets.name)
        assertNull(project.config.directories.custom)
    }

    @Test
    fun `minimal yaml file with own directories defined`() {
        val project = Project(yaml.OWN_DIRS)

        assertNotNull(project)
        assertEquals("alpha", project.config.directories.sources.name)
        assertEquals("beta", project.config.directories.output.name)
        assertEquals("gamma", project.config.directories.templates.name)
        assertEquals("delta", project.config.directories.assets.name)
        assertNull(project.config.directories.custom)
    }

    @Test
    fun `yaml provides an array of standard generator names`() {
        val project = Project(yaml.CUSTOM_GENERATOR_PIPELINE)
        assertNotNull(project)

        assertNotNull(project.config)
        assertNotNull(project.config.generators)
        assertEquals(3, project.config.generators!!.size)
        val generators: ArrayList<String> = project.config.generators!!
        val generatorNames = generators.map { it.substringAfterLast(".") }
        assert(generatorNames.contains(Expected.generator_index))
        assert(generatorNames.contains(Expected.generator_nav))
        assert(generatorNames.contains(Expected.generator_google))
    }

    @Test
    fun `yaml provides distinct postLayout configuration`() {
        val project = Project(yaml.CUSTOM_POST_LAYOUTS)
        assertNotNull(project)

        assertNotNull(project.config)
        assertNotNull(project.config.postLayouts)
        assertEquals(2, project.config.postLayouts.size)
        assert(project.config.postLayouts.contains(Expected.layout_genre))
        assert(project.config.postLayouts.contains(Expected.layout_composer))
        assert(!project.config.postLayouts.contains(Expected.layout_default))
    }

    @Test
    fun `custom properties are added to the project model`() {
        val project = Project(yaml.CUSTOM_PROPERTIES)
        assertNotNull(project)
        assertEquals("Liam", project.customAttributes["author"])
        assertEquals(true, project.customAttributes["debug"])
    }
}

object yaml {
    val EMPTY = """

	""".trimIndent()

    val MINIMAL = """
		--- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
			source: sources
			output: output
			templates: templates
			assets: assets
	""".trimIndent().replace("\t", "  ")

    val INVALID_DIRECTORIES = """
		--- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
	""".trimIndent().replace("\t", "  ")

    val OWN_DIRS = """
		--- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
			source: alpha
			output: beta
			templates: gamma
			assets: delta
	""".trimIndent().replace("\t", "  ")

    val CUSTOM_GENERATOR_PIPELINE = """
        --- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
			source: sources
			output: site
			templates: templates
			assets: assets
		generators: [IndexPageGenerator, PostNavigationGenerator, org.google.sitemapxml.Generator]
	""".trimIndent().replace("\t", "  ")

    val CUSTOM_POST_LAYOUTS = """
          --- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
			source: sources
			output: site
			templates: templates
			assets: assets
		postLayouts: [composer,genre]
	""".trimIndent().replace("\t", "  ")

    val CUSTOM_PROPERTIES = """
          --- # Project properties
		name: minimalTest
		theme: bulma
		--- # Project configuration
		directories:
			source: sources
			output: site
			templates: templates
			assets: assets
		postLayouts: [composer,genre]
        --- # Custom properties
        author: Liam
        debug: true
	""".trimIndent().replace("\t", "  ")
}

object Expected {
    const val sources = "sources"
    const val output = "output"
    const val templates = "templates"
    const val assets = "assets"
    const val name = "minimalTest"
    const val theme = "bulma"

    val generator_index = "IndexPageGenerator"
    val generator_nav = "PostNavigationGenerator"
    val generator_taxonmy = "TaxonomyNavigationGenerator"
    val generator_google = "Generator"
    val layout_genre = "genre"
    val layout_composer = "composer"
    val layout_default = "post"
}
