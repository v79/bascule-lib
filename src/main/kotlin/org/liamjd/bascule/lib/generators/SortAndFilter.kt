package org.liamjd.bascule.lib.generators

import org.liamjd.bascule.lib.model.Post
import org.liamjd.bascule.lib.model.Project

interface SortAndFilter {

	fun sortAndFilter(project: Project,posts: List<Post>): List<List<Post>>
}
