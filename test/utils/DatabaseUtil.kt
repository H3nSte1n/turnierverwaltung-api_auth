import org.jetbrains.exposed.sql.Column
import org.jetbrains.exposed.sql.Table
import kotlin.test.junit5.JUnit5Asserter

object DatabaseUtil {
    fun getDatabaseColumn(name: String, table: Table): Column<*>? {
        return table.columns.find { it.name == name }
    }

    fun assertColumnExist(name: String, table: Table) {
        val column = getDatabaseColumn(name, table)
        JUnit5Asserter.assertNotNull("column $name should exist", column)
    }

    fun assertColumnType(name: String, table: Table, sqlType: String) {
        val column = getDatabaseColumn(name, table)

        JUnit5Asserter.assertEquals(
            "$name column has type $sqlType",
            sqlType.toLowerCase(),
            column!!.columnType.sqlType().toLowerCase()
        )
    }
}
