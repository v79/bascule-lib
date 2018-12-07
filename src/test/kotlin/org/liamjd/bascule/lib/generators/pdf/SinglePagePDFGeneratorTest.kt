/*
package org.liamjd.bascule.lib.generators.pdf

import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.liamjd.bascule.lib.FileHandler
import org.liamjd.bascule.lib.model.Project
import org.liamjd.bascule.lib.render.Renderer
import org.spekframework.spek2.Spek
import java.io.File
import java.net.URL
import kotlin.test.assertTrue

class SinglePagePDFGeneratorTest : Spek({
    group("Can compile and run but do nothing useful") {

        val generator by memoized { SinglePagePDFGenerator() }
        lateinit var project: Project
        val mRenderer = mockk<Renderer>()
        val mFileHandler = mockk<FileHandler>()

        var foFileAfterHandlebars: String = this.javaClass.getResource("/fullPDF-handlebars-test1.fo").readText()

        beforeEachTest {
            project = makeProject()

            every { mRenderer.render(project.model, generator.TEMPLATE) }.returns(foFileAfterHandlebars)
        }

        test("apache fop can render an example PDF") {
            // setup

            // execute
            runBlocking {
                launch {
//                    generator.process(project, mRenderer, mFileHandler)
                }
            }
            // verify
            assertTrue { true }
        }


    }
}) {

}

fun makeProject(): Project {
    val testRoot = File("pdfGenTest")
    var yamlString = ""
    val testConfig = mapOf<String, Any>()
    val project =
        Project("pdfGenTest", testRoot, "source", "output", "assets", "templates", yamlString, "testTheme", testConfig)

    return project
}
*/
