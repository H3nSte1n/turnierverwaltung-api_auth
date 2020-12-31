package config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.github.cdimascio.dotenv.Dotenv
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import schemas.Users
import java.util.*

object DatabaseFactory {
    lateinit var database: Database
    fun init(dotenv: Dotenv) {
        database = Database.Companion.connect(config(dotenv))

        transaction {
            exec(
                """
                    DO ${'$'}${'$'} BEGIN
                        CREATE TYPE UserRole AS ENUM ('admin', 'user');
                    EXCEPTION
                        WHEN duplicate_object THEN null;
                    END ${'$'}${'$'};
                """.trimIndent()
            )
            SchemaUtils.create(Users)
        }
    }

    private fun config(dotenv: Dotenv): HikariDataSource {
        val props = Properties()
        props.setProperty("dataSourceClassName", "org.postgresql.ds.PGSimpleDataSource")
        setProperty(props, "user", dotenv["USER"] ?: "root")
        setProperty(props, "password", dotenv["PASSWORD"] ?: "")
        setProperty(props, "databaseName", dotenv["DATABASE"] ?: "authentication")
        setProperty(props, "portNumber", dotenv["PORT"] ?: "5432")
        setProperty(props, "serverName", dotenv["SERVER"] ?: "localhost")
        props.put("javax.persistence.create-database-schemas", true)

        val config = HikariConfig(props)
        config.validate()

        return HikariDataSource(config)
    }

    private fun setProperty(props: Properties, property: String, value: String) {
        props.setProperty("dataSource.$property", value)
    }
}
