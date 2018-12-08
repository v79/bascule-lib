package org.liamjd.bascule.lib.model

import java.io.File

class Directories(val root: File, val sources: File, val output: File, val assets: File, val templates: File, val custom: Map<String, File>?) {

	object Defaults {
		fun get(root: File): Directories {
			return Directories(root, File(root, "sources"), File(root, "output"), File(root, "assets"), File(root, "templates"), null)
		}
	}

	override fun toString() : String {
		return "Directories: sources = ${sources}\noutput = ${output}\nassets = ${assets}\ntemplates = $templates\ncustom = $custom\n"
	}

}
