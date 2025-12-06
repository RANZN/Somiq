package com.ranjan.somiq.core.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val id: Long,
    val name: String,
    val age: Int,
)
