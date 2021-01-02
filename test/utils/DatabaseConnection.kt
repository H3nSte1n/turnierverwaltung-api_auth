import TestUtils.env
import config.DatabaseFactory
import org.jetbrains.exposed.sql.transactions.transactionManager
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll

abstract class DatabaseConnection {

    companion object {

        @BeforeAll
        @JvmStatic
        fun connectToDatabase() {
            DatabaseFactory.init(env)
        }

        @AfterAll
        @JvmStatic
        fun disconnectoToDatabase() {
            DatabaseFactory.database.transactionManager.currentOrNull()?.close()
        }
    }
}
