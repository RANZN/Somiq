package com.ranjan.somiq.feed.data.cache

import com.ranjan.somiq.feed.domain.model.Post
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

class InMemoryPostCache {
    private val _myPostsFlow = MutableStateFlow<List<Post>>(emptyList())
    val myPostsFlow: StateFlow<List<Post>> = _myPostsFlow.asStateFlow()

    private val _bookmarkedPostsFlow = MutableStateFlow<List<Post>>(emptyList())
    val bookmarkedPostsFlow: StateFlow<List<Post>> = _bookmarkedPostsFlow.asStateFlow()

    private val postsMap = mutableMapOf<String, Post>()
    private val mutex = Mutex()

    suspend fun cachePosts(posts: List<Post>) {
        mutex.withLock {
            posts.forEach { postsMap[it.id] = it }
        }
    }

    suspend fun cachePost(post: Post) {
        mutex.withLock {
            postsMap[post.id] = post
        }
    }

    suspend fun getPost(postId: String): Post? {
        return mutex.withLock {
            postsMap[postId]
        }
    }

    suspend fun setMyPosts(posts: List<Post>) {
        mutex.withLock {
            posts.forEach { postsMap[it.id] = it }
            _myPostsFlow.update { posts }
        }
    }

    suspend fun addPostToMyPosts(post: Post) {
        mutex.withLock {
            postsMap[post.id] = post
            _myPostsFlow.update { current -> listOf(post) + current }
        }
    }

    suspend fun setBookmarkedPosts(posts: List<Post>) {
        mutex.withLock {
            posts.forEach { postsMap[it.id] = it }
            _bookmarkedPostsFlow.update { posts }
        }
    }

    suspend fun updateLikeStatus(postId: String, isLiked: Boolean, likesCount: Long) {
        mutex.withLock {
            val post = postsMap[postId]
            if (post != null) {
                val updated = post.copy(isLiked = isLiked, likesCount = likesCount)
                postsMap[postId] = updated
                updateFlowsWithPost(updated)
            }
        }
    }

    suspend fun updateBookmarkStatus(postId: String, isBookmarked: Boolean, bookmarksCount: Long) {
        mutex.withLock {
            val post = postsMap[postId]
            if (post != null) {
                val updated = post.copy(isBookmarked = isBookmarked, bookmarksCount = bookmarksCount)
                postsMap[postId] = updated
                updateFlowsWithPost(updated)

                if (isBookmarked) {
                    _bookmarkedPostsFlow.update { current ->
                        if (current.none { it.id == postId }) current + updated else current
                    }
                } else {
                    _bookmarkedPostsFlow.update { current ->
                        current.filter { it.id != postId }
                    }
                }
            } else {
                if (!isBookmarked) {
                    _bookmarkedPostsFlow.update { current ->
                        current.filter { it.id != postId }
                    }
                }
            }
        }
    }

    private fun updateFlowsWithPost(updatedPost: Post) {
        _myPostsFlow.update { current ->
            current.map { if (it.id == updatedPost.id) updatedPost else it }
        }
        _bookmarkedPostsFlow.update { current ->
            current.map { if (it.id == updatedPost.id) updatedPost else it }
        }
    }
}
