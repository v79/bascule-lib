package org.liamjd.bascule.lib.generators

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.liamjd.bascule.lib.model.Project
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

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
		assertEquals("minimalTest",project.model["siteName"])
		assertEquals(expected.theme,project.theme)
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
		assertNotNull(project.dirs.custom) {
			assert(it.containsKey("pdf"))
			assertEquals(it["pdf"]?.absoluteFile?.name,"pdfsGoHere")
		}
	}

	@Test
	fun `yaml has a mix of own dirs and custom directories`() {
		val project = Project(yaml.MIX_OF_DIRS)

		assertNotNull(project)
		assertEquals("episilon", project.dirs.sources.name)
		assertEquals(expected.output, project.dirs.output.name)
		assertEquals(expected.templates, project.dirs.templates.name)
		assertEquals(expected.assets, project.dirs.assets.name)

		assertNotNull(project.dirs.custom) {
			assert(it.containsKey("pdf"))
			assertEquals(it["pdf"]?.absoluteFile?.name,"customPDF")
		}
	}

	@Test
	fun `yaml provides an array of standard generator names`() {
		val project = Project(yaml.CUSTOM_GENERATOR_PIPELINE)
		assertNotNull(project)

		assertNotNull(project.configMap)
		assertNotNull(project.generators) {
			assertEquals(3,it.size)
			assert(it.contains(expected.generator_index))
			assert(it.contains(expected.generator_nav))
			assert(it.contains(expected.generator_google))
		}
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
	""".replace("\t","  ")

	val CUSTOM_ONLY = """
		directories:
			custom:
				pdf: pdfsGoHere
	""".replace("\t","  ")

	val MIX_OF_DIRS = """
		siteName: mixedDirs
		directories:
			source: episilon
			custom:
				pdf: customPDF
	""".replace("\t","  ")

	val CUSTOM_GENERATOR_PIPELINE = """
		siteName: customeGenPipeline
		generators: [IndexPageGenerator, PostNavigationGenerator, org.google.sitemapxml.Generator]
	""".replace("\t","  ")
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
}
