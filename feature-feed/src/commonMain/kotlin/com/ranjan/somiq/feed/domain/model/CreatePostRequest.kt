package com.ranjan.somiq.feed.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class CreatePostRequest(
    val caption: String,
    val mediaUrls: List<PostMedia> = emptyList()
)

@Serializable
data class PostMedia(
    val name: String,
    val byte: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PostMedia

        if (name != other.name) return false
        if (!byte.contentEquals(other.byte)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = name.hashCode()
        result = 31 * result + byte.contentHashCode()
        return result
    }
}
