package org.liamjd.bascule.lib.render

interface TemplatePageRenderer {
    fun render(model: Map<String,Any?>, templateName: String) : String
}
