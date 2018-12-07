package org.liamjd.bascule.lib.model

import java.time.LocalDate

interface Post {
    var sourceFileName: String
    var url: String
    var title: String
    var author: String

    var layout: String
    var date: LocalDate

    var tags: MutableSet<Tag>
    var slug: String
    var attributes: MutableMap<String, Any>

    var content: String
    var newer: PostLink?
    var older: PostLink?

    var rawContent: String // for the original markdown, still contains the yaml though

    fun getSummary(characterCount: Int = 150): String

    fun toModel(): Map<String, Any?> {
        val modelMap = mutableMapOf<String, Any?>()
        modelMap.put("sourceFileName.name", sourceFileName)
        modelMap.put("url", url)
        modelMap.put("title", title)
        modelMap.put("author", author)
        modelMap.put("layout", layout)
        modelMap.put("date", date)
        modelMap.put("tags", tags)
        modelMap.put("slug", slug)
        modelMap.put("attributes", attributes)
        modelMap.put("newer", newer)
        modelMap.put("older", older)

        return modelMap
    }
}
