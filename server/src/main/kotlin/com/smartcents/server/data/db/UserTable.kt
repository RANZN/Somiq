package com.smartcents.server.data.db

import org.jetbrains.exposed.sql.Table
import java.util.UUID

object UserTable : Table() {
    val id = uuid("id").clientDefault { UUID.randomUUID() }
    val email = varchar("email", 255).uniqueIndex()
    val name = varchar("name", 255)
    val password = varchar("password", 255)
    override val primaryKey = PrimaryKey(id)
}