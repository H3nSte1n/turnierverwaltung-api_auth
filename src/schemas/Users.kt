package schemas

import com.turnierverwaltung_api_auth.enums.UserRole
import data.User
import helper.PGEnum
import helper.Password.generateHash
import helper.Password.generateSalt
import org.jetbrains.exposed.dao.UUIDTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction

object Users : UUIDTable() {
    val name: Column<String> = varchar("name", 60)
    val email: Column<String?> = varchar("email", 120).nullable()
    val role: Column<UserRole> = customEnumeration(
        "role",
        "UserRole",
        { UserRole.valueOf(it as String) },
        { PGEnum("UserRole", it) }
    )

    val passwordHash: Column<String> = varchar("password_hash", 160)
    val salt: Column<String> = varchar("salt", 29)

    fun findUser(customName: String): User {
        return transaction {
            select { name eq customName }.first()
        }.let { it ->
            User(it[id], it[name], it[email], it[passwordHash], it[salt], it[role])
        }
    }

    fun userExist(customName: String): Boolean {
        return transaction {
            select { name eq customName }.empty().not()
        }
    }

    fun createUser(user: User): User {
        val generatedSalt = generateSalt()

        return transaction {
            insert {
                it[name] = user.name
                it[email] = user.email
                it[salt] = generatedSalt
                it[passwordHash] = generateHash(user.password, generatedSalt)
                it[role] = user.role!!
            }
        }.let {
            User(it[id], it[name], it[email], it[passwordHash], it[salt], it[role])
        }
    }
}
