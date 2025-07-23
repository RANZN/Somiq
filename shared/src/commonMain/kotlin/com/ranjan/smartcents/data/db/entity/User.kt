package com.ranjan.smartcents.data.db.entity

import androidx.room.Entity


@Entity
data class User(
    val name: String,
    val age: Int,
)
