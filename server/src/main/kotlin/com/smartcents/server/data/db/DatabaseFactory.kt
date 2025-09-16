package com.smartcents.server.data.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(config: ApplicationConfig) {
        val dbConfig = config.config("db")

        val driverClassName = dbConfig.property("driver").getString()
        val jdbcURL = dbConfig.property("url").getString()
        val user = dbConfig.property("user").getString()
        val dbPassword = dbConfig.property("password").getString()

        Database.connect(createHikariDataSource(jdbcURL, driverClassName, user, dbPassword))
        transaction {
            SchemaUtils.create(UserTable)
        }
    }

    private fun createHikariDataSource(
        url: String,
        driver: String,
        user: String,
        password: String
    ) = HikariDataSource(HikariConfig().apply {
        driverClassName = driver
        jdbcUrl = url
        username = user
        this.password = password
        maximumPoolSize = 3
        isAutoCommit = false
        transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        validate()
    })

    /**
     * This is the helper function you asked about.
     * It runs the database query block on a dedicated IO thread pool.
     */
    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}