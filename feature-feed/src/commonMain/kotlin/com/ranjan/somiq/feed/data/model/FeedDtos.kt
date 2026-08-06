package com.ranjan.somiq.feed.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostDto(
    @SerialName("postId")
    val id: String,
    val caption: String,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val updatedAt: Long?,
    val mediaUrls: List<String>,
    val likesCount: Long,
    val bookmarksCount: Long,
    val isLiked: Boolean = false,
    val isBookmarked: Boolean = false
)

@Serializable
data class StoryDto(
    @SerialName("storyId")
    val id: String,
    val mediaUrl: String,
    val mediaType: MediaTypeDto,
    @SerialName("authorId")
    val authorId: String,
    val authorName: String,
    val authorUsername: String?,
    val authorProfilePictureUrl: String?,
    val createdAt: Long,
    val expiresAt: Long,
    val viewsCount: Long,
    val isViewed: Boolean = false
)

@Serializable
enum class MediaTypeDto {
    IMAGE,
    VIDEO
}

@Serializable
data class CreateStoryRequestDto(
    val mediaUrl: String,
    val mediaType: MediaTypeDto
)

@Serializable
data class CreatePostRequestDto(
    val caption: String,
    val mediaUrls: List<PostMediaDto> = emptyList()
)

@Serializable
data class PostMediaDto(
    val name: String,
    val byte: ByteArray,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as PostMediaDto

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

@Serializable
data class ToggleResponseDto(
    val isLiked: Boolean,
    val isBookmarked: Boolean,
    val likesCount: Long,
    val bookmarksCount: Long
)
