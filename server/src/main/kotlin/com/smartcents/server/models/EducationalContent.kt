package com.smartcents.server.models

import kotlinx.serialization.Serializable

@Serializable
data class Quote(
    val id: Int,
    val text: String,
    val author: String,
    val category: String
)

@Serializable
data class Article(
    val id: Int,
    val title: String,
    val content: String,
    val author: String,
    val publishDate: String,
    val category: String,
    val tags: List<String>
)

@Serializable
data class Tip(
    val id: Int,
    val title: String,
    val content: String,
    val category: String
)