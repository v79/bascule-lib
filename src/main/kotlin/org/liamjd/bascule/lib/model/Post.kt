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
        modelMap["sourceFileName"] = sourceFileName
        modelMap["url"] = url
        modelMap["title"] = title
        modelMap["author"] = author
        modelMap["layout"] = layout
        modelMap["date"] = date
        modelMap["tags"] = tags
        modelMap["slug"] = slug
        modelMap["attributes"] = attributes
        modelMap["newer"] = newer
        modelMap["older"] = older

        return modelMap
    }

    fun groupTagsByCategory(): Map<out String, Set<Tag>?>
}
