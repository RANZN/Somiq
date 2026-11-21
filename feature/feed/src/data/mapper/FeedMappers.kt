package com.ranjan.somiq.feed.data.mapper

import com.ranjan.somiq.feed.data.model.*
import com.ranjan.somiq.feed.domain.model.*

fun MediaTypeDto.toDomain(): MediaType = when (this) {
    MediaTypeDto.IMAGE -> MediaType.IMAGE
    MediaTypeDto.VIDEO -> MediaType.VIDEO
}

fun MediaType.toDto(): MediaTypeDto = when (this) {
    MediaType.IMAGE -> MediaTypeDto.IMAGE
    MediaType.VIDEO -> MediaTypeDto.VIDEO
}

fun PostDto.toDomain(): Post = Post(
    id = id,
    caption = caption,
    authorId = authorId,
    authorName = authorName,
    authorUsername = authorUsername,
    authorProfilePictureUrl = authorProfilePictureUrl,
    createdAt = createdAt,
    updatedAt = updatedAt,
    mediaUrls = mediaUrls,
    likesCount = likesCount,
    bookmarksCount = bookmarksCount,
    isLiked = isLiked,
    isBookmarked = isBookmarked
)

fun StoryDto.toDomain(): Story = Story(
    id = id,
    mediaUrl = mediaUrl,
    mediaType = mediaType.toDomain(),
    authorId = authorId,
    authorName = authorName,
    authorUsername = authorUsername,
    authorProfilePictureUrl = authorProfilePictureUrl,
    createdAt = createdAt,
    expiresAt = expiresAt,
    viewsCount = viewsCount,
    isViewed = isViewed
)

fun CreateStoryRequest.toDto(): CreateStoryRequestDto = CreateStoryRequestDto(
    mediaUrl = mediaUrl,
    mediaType = mediaType.toDto()
)

fun CreatePostRequest.toDto(): CreatePostRequestDto = CreatePostRequestDto(
    caption = caption,
    mediaUrls = media.mapIndexed { index, bytes ->
        PostMediaDto(
            name = "media_$index.jpg",
            byte = bytes
        )
    }
)

fun ToggleResponseDto.toDomain(): ToggleResponse = ToggleResponse(
    isLiked = isLiked,
    isBookmarked = isBookmarked,
    likesCount = likesCount,
    bookmarksCount = bookmarksCount
)
