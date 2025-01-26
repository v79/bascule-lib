package org.liamjd.bascule.lib.model

typealias TagCategory = String

data class Tag(val category: TagCategory, val label: String, var url: String, var postCount: Int = 1, var hasPosts: Boolean = false) {
    // I don't care about postCount, etc when storing in a set
    override fun equals(other: Any?): Boolean {
        if(other is Tag) {
            if(this.label == other.label && (this.category == other.category)) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return this.category.hashCode() + this.label.hashCode() + this.url.hashCode()
    }

}
