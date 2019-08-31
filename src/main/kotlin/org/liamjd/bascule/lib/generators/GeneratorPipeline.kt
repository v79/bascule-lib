package org.liamjd.bascule.lib.generators

import org.liamjd.bascule.lib.FileHandler
import org.liamjd.bascule.lib.model.Project
import org.liamjd.bascule.lib.render.TemplatePageRenderer

interface GeneratorPipeline {

    val TEMPLATE: String
    suspend fun process(project: Project, renderer: TemplatePageRenderer, fileHandler: FileHandler)
}
