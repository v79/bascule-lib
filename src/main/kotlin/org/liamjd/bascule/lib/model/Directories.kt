package org.liamjd.bascule.lib.model

import java.io.File

class Directories(
    val root: File,
    val sources: File,
    val output: File,
    val assets: File,
    val templates: File,
    val custom: Map<String, File>?,
) {

    object Defaults {
        fun get(root: File): Directories {
            return Directories(
                root,
                sources = File(root, "sources"),
                output = File(root, "output"),
                assets = File(root, "assets"),
                templates = File(root, "templates"),
                custom = null
            )
        }
    }

    override fun toString(): String {
        return "Directories: sources = ${sources}\noutput = ${output}\nassets = ${assets}\ntemplates = $templates\ncustom = $custom\n"
    }

}
