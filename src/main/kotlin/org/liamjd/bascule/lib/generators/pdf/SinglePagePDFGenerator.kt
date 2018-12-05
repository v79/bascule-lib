package org.liamjd.bascule.lib.generators.pdf

import org.apache.fop.apps.FopFactory
import org.apache.fop.apps.MimeConstants
import org.liamjd.bascule.lib.FileHandler
import org.liamjd.bascule.lib.generators.GeneratorPipeline
import org.liamjd.bascule.lib.model.Project
import org.liamjd.bascule.lib.render.Renderer
import java.io.FileOutputStream
import java.net.URL
import javax.xml.transform.TransformerFactory
import javax.xml.transform.sax.SAXResult
import javax.xml.transform.stream.StreamSource


class SinglePagePDFGenerator : GeneratorPipeline {
    override val TEMPLATE: String
        get() = "fullpdf"


    val fopFactory = FopFactory.newInstance(readFileFromResources("/", "fopConf.xconf").toURI())

    override suspend fun process(project: Project, renderer: Renderer, fileHandler: FileHandler) {

        val outputFilename = "out/test/${project.name}.pdf"
        FileOutputStream(outputFilename).use { outStream ->
            val fop = fopFactory.newFop(MimeConstants.MIME_PDF, outStream)

            val transformerFactory = TransformerFactory.newInstance()
            val transformer = transformerFactory.newTransformer()

            // Ask handlebars to populate the .fo file with the project model before passing to FOP to convert to PDF
            val renderedFOString = renderer.render(project.model, TEMPLATE)

            val fopInputStream = renderedFOString.byteInputStream()
            val src = StreamSource(fopInputStream)
            val res = SAXResult(fop.defaultHandler)
            transformer.transform(src, res)
        }
    }

    fun readFileFromResources(sourceDir: String, fileName: String): URL {
        return javaClass.getResource(sourceDir + fileName)
    }
}
