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
	fun `builds project with minimal yaml file`() {
		val project = Project(yaml.MINIMAL)

		assertNotNull(project)
		assertEquals("minimalTest", project.model["siteName"])
		assertEquals(expected.theme, project.theme)
		assertEquals(expected.layout_default, project.postLayouts.first())
	}

	@Test
	fun `minimal yaml file has default directories defined`() {
		val project = Project(yaml.MINIMAL)

		assertNotNull(project)
		assertEquals(expected.sources, project.dirs.sources.name)
		assertEquals(expected.output, project.dirs.output.name)
		assertEquals(expected.templates, project.dirs.templates.name)
		assertEquals(expected.assets, project.dirs.assets.name)
		assertNull(project.dirs.custom)
	}

	@Test
	fun `minimal yaml file with own directories defined`() {
		val project = Project(yaml.OWN_DIRS)

		assertNotNull(project)
		assertEquals("alpha", project.dirs.sources.name)
		assertEquals("beta", project.dirs.output.name)
		assertEquals("gamma", project.dirs.templates.name)
		assertEquals("delta", project.dirs.assets.name)
		assertNull(project.dirs.custom)
	}

	@Test
	fun `yaml only has custom directories, rest are default`() {
		val project = Project(yaml.CUSTOM_ONLY)

		assertNotNull(project)
		assertEquals(expected.sources, project.dirs.sources.name)
		assertEquals(expected.output, project.dirs.output.name)
		assertEquals(expected.templates, project.dirs.templates.name)
		assertEquals(expected.assets, project.dirs.assets.name)
		assertNotNull(project.dirs.custom)
		assert(project.dirs.custom!!.containsKey("pdf"))
		val pdf = project.dirs.custom!!["pdf"]!!.absoluteFile.name
		assertEquals(pdf, "pdfsGoHere")

	}

	@Test
	fun `yaml has a mix of own dirs and custom directories`() {
		val project = Project(yaml.MIX_OF_DIRS)

		assertNotNull(project)
		assertEquals("episilon", project.dirs.sources.name)
		assertEquals(expected.output, project.dirs.output.name)
		assertEquals(expected.templates, project.dirs.templates.name)
		assertEquals(expected.assets, project.dirs.assets.name)

		assertNotNull(project.dirs.custom)
		assert(project.dirs.custom!!.containsKey("pdf"))
		val pdf = project.dirs.custom!!["pdf"]!!.absoluteFile.name
		assertEquals("customPDF", pdf)
	}

	@Test
	fun `yaml provides an array of standard generator names`() {
		val project = Project(yaml.CUSTOM_GENERATOR_PIPELINE)
		assertNotNull(project)

		assertNotNull(project.configMap)
		assertNotNull(project.generators)
		assertEquals(3, project.generators!!.size)
		assert(project.generators!!.contains(expected.generator_index))
		assert(project.generators!!.contains(expected.generator_nav))
		assert(project.generators!!.contains(expected.generator_google))
	}

	@Test
	fun `yaml provides distinct postLayout configuration`() {
		val project = Project(yaml.CUSTOM_POST_LAYOUTS)
		assertNotNull(project)

		assertNotNull(project.configMap)
		assertNotNull(project.postLayouts)
		assertEquals(2, project.postLayouts.size)
		assert(project.postLayouts.contains(expected.layout_genre))
		assert(project.postLayouts.contains(expected.layout_composer))
		assert(!project.postLayouts.contains(expected.layout_default))
	}
}

object yaml {
	val EMPTY = """

	""".trimIndent()

	val MINIMAL = """
		siteName: minimalTest
	""".trimIndent()

	val OWN_DIRS = """
		siteName: ownDirs
		directories:
			source: alpha
			output: beta
			templates: gamma
			assets: delta
	""".replace("\t", "  ")

	val CUSTOM_ONLY = """
		directories:
			custom:
				pdf: pdfsGoHere
	""".replace("\t", "  ")

	val MIX_OF_DIRS = """
		siteName: mixedDirs
		directories:
			source: episilon
			custom:
				pdf: customPDF
	""".replace("\t", "  ")

	val CUSTOM_GENERATOR_PIPELINE = """
		siteName: customeGenPipeline
		generators: [IndexPageGenerator, PostNavigationGenerator, org.google.sitemapxml.Generator]
	""".replace("\t", "  ")

	val CUSTOM_POST_LAYOUTS = """
		postLayouts: [composer,genre]
	""".replace("\t", "  ")
}

object expected {
	val sources = "sources"
	val output = "output"
	val templates = "templates"
	val assets = "assets"

	val theme = "bulma"

	val generator_index = "org.liamjd.bascule.pipeline.IndexPageGenerator"
	val generator_nav = "org.liamjd.bascule.pipeline.PostNavigationGenerator"
	val generator_taxonmy = "org.liamjd.bascule.pipeline.TaxonomyNavigationGenerator"
	val generator_google = "org.google.sitemapxml.Generator"
	val layout_genre = "genre"
	val layout_composer = "composer"
	val layout_default = "post"
}
