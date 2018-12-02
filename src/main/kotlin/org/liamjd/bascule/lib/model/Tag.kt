package org.liamjd.bascule.lib.model

data class Tag(val label: String, var url: String, var postCount: Int = 0, var hasPosts: Boolean = false) {
    // I don't care about postCount, etc when storing in a set
    override fun equals(other: Any?): Boolean {
        if(other is Tag) {
            if(this.label.equals(other.label)) {
                return true
            }
        }
        return false
    }

    override fun hashCode(): Int {
        return this.label.hashCode() + this.url.hashCode()
    }
}
