package com.smartcents.server.domain.model

import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    val id: Int,
    val amount: Double,
    val description: String,
    val category: String,
    val date: String
)