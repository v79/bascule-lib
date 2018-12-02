package org.liamjd.bascule.lib.render

interface Renderer {
    fun render(model: Map<String,Any?>, templateName: String) : String
}
