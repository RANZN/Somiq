package com.ranjan.somiq.feed.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import com.ranjan.somiq.core.util.toTimeAgo
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.ranjan.somiq.core.presentation.component.AppAsyncImage
import com.ranjan.somiq.feed.domain.model.Post
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.layout.height

@Composable
fun PostItem(
    post: Post,
    modifier: Modifier = Modifier,
    onLikeClick: () -> Unit = {},
    onCommentClick: () -> Unit = {},
    onShareClick: () -> Unit = {},
    onSaveClick: () -> Unit = {},
    onMoreClick: () -> Unit = {},
    onUserClick: () -> Unit = {}
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        PostHeader(
            username = post.authorUsername ?: post.authorName,
            userAvatar = post.authorProfilePictureUrl,
            location = "",
            onUserClick = onUserClick,
            onMoreClick = onMoreClick
        )

        // Display post images with swipeable pager and double-tap to like
        if (post.mediaUrls.isNotEmpty()) {
            val pagerState = rememberPagerState(pageCount = { post.mediaUrls.size })
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(380.dp)
                ) { page ->
                    DoubleTapToLikeBox(
                        modifier = Modifier.fillMaxSize(),
                        onDoubleTap = onLikeClick
                    ) {
                        AppAsyncImage(
                            imageUrl = post.mediaUrls[page],
                            contentDescription = "Post image $page",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }
                }

                // Dot Indicators
                if (post.mediaUrls.size > 1) {
                    Row(
                        modifier = Modifier
                            .padding(bottom = 12.dp)
                            .background(
                                color = Color.Black.copy(alpha = 0.5f),
                                shape = RoundedCornerShape(100)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        repeat(post.mediaUrls.size) { index ->
                            val active = pagerState.currentPage == index
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .clip(CircleShape)
                                    .background(
                                        color = if (active)
                                            MaterialTheme.colorScheme.primary
                                        else
                                            Color.White.copy(alpha = 0.6f)
                                    )
                            )
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = post.caption,
                    style = MaterialTheme.typography.displayMedium
                )
            }
        }

        PostActions(
            isLiked = post.isLiked,
            isSaved = post.isBookmarked,
            onLikeClick = onLikeClick,
            onCommentClick = onCommentClick,
            onShareClick = onShareClick,
            onSaveClick = onSaveClick
        )

        if (post.likesCount > 0) {
            Text(
                text = "${post.likesCount} likes",
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        PostCaption(
            username = post.authorUsername ?: post.authorName,
            caption = post.caption,
            onUserClick = onUserClick
        )

        Text(
            text = post.createdAt.toTimeAgo(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )

        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
private fun PostHeader(
    username: String,
    userAvatar: String?,
    location: String?,
    onUserClick: () -> Unit,
    onMoreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier.clickable(onClick = onUserClick),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (userAvatar != null) {
                AppAsyncImage(
                    imageUrl = userAvatar,
                    contentDescription = "Profile picture",
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop,
                    error = {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.surfaceVariant),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = username.take(1).uppercase(),
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.surfaceVariant),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = username.take(1).uppercase(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = username,
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (location != null) {
                    Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        IconButton(onClick = onMoreClick) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More options",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PostActions(
    isLiked: Boolean,
    isSaved: Boolean,
    onLikeClick: () -> Unit,
    onCommentClick: () -> Unit,
    onShareClick: () -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            IconButton(onClick = onLikeClick) {
                Icon(
                    imageVector = if (isLiked) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isLiked) "Unlike" else "Like",
                    tint = if (isLiked) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onCommentClick) {
                Icon(
                    imageVector = Icons.Default.ChatBubbleOutline,
                    contentDescription = "Comment",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = onShareClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Share",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        IconButton(onClick = onSaveClick) {
            Icon(
                imageVector = if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                contentDescription = if (isSaved) "Unsave" else "Save",
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun PostCaption(
    username: String,
    caption: String,
    onUserClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
            append(username)
        }
        append(" $caption")
    }

    Text(
        text = annotatedText,
        style = MaterialTheme.typography.bodyMedium,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
            .clickable { onUserClick() },
        maxLines = 2,
        overflow = TextOverflow.Ellipsis,
        color = MaterialTheme.colorScheme.onSurface
    )
}
