package schemas

import DatabaseConnection
import DatabaseUtil.assertColumnExist
import DatabaseUtil.assertColumnType
import com.turnierverwaltung_api_auth.enums.UserRole
import data.User
import helper.Password
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mindrot.jbcrypt.BCrypt
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UsersTest : DatabaseConnection() {

    @BeforeTest
    fun prepare() {
        prepareTables()
    }

    @Test
    @DisplayName("All Columns are avaiable")
    fun testColumnsExist() {
        transaction {
            assertColumnExist("id", Users)
            assertColumnExist("name", Users)
            assertColumnExist("email", Users)
            assertColumnExist("salt", Users)
            assertColumnExist("password_hash", Users)
            assertColumnExist("role", Users)
        }
    }

    @Test
    @DisplayName("All Columns has the correct sql-type")
    fun testColumnsType() {
        transaction {
            assertColumnType("id", Users, "uuid")
            assertColumnType("name", Users, "varchar(60)")
            assertColumnType("email", Users, "varchar(120)")
            assertColumnType("salt", Users, "varchar(29)")
            assertColumnType("password_hash", Users, "varchar(160)")
            assertColumnType("role", Users, "userrole")
        }
    }

    @Test
    @DisplayName("Method createUser will generate a new factories.User and store them into database")
    fun testCreateUser() {
        val user = User(
            null,
            "foo",
            "foo@bar.com",
            "foobar",
            null,
            UserRole.admin
        )
        Users.createUser(user)
        val persistedUser = transaction {
            Users.select { Users.name eq user.name }.first()
        }.let { it ->
            User(it[Users.id], it[Users.name], it[Users.email], it[Users.passwordHash], it[Users.salt], it[Users.role])
        }

        assertNotNull(persistedUser.id, "id should not be empty")
        assertEquals("foo", persistedUser.name)
        assertEquals("foo@bar.com", persistedUser.email)
        assertEquals(BCrypt.hashpw("foobar", persistedUser.salt), persistedUser.password)
        assertNotNull(persistedUser.salt, "salt should not be empty")
        assertEquals(UserRole.admin, persistedUser.role)
    }

    @Test
    @DisplayName("Method findUser should find factories.User by name")
    fun testFindUser() {
        val user = createAndStoreUser()

        val persistedUser = Users.findUser(user.name)
        assertNotNull(persistedUser.id, "id should not be empty")
        assertEquals(user.name, persistedUser.name)
        assertEquals(user.email, persistedUser.email)
        assertEquals(user.password, persistedUser.password)
        assertEquals(user.salt, persistedUser.salt)
        assertEquals(user.role, persistedUser.role)
    }

    @Test
    @DisplayName("Method userExist should return boolean value")
    fun testUserExist() {
        var isUserExists = Users.userExist("foobar")
        assertEquals(isUserExists, false)

        val user = createAndStoreUser()
        isUserExists = Users.userExist(user.name)
        assertEquals(isUserExists, true)
    }

    private fun createAndStoreUser(): User {
        val generatedSalt = Password.generateSalt()
        val user = User(
            null,
            "foo",
            "foo@bar.com",
            Password.generateHash("foo", generatedSalt),
            generatedSalt,
            UserRole.admin
        )

        transaction {
            Users.insert {
                it[Users.name] = user.name
                it[Users.email] = user.email
                it[Users.salt] = user.salt as String
                it[Users.passwordHash] = user.password
                it[Users.role] = user.role!!
            }
        }

        return user
    }

    private fun prepareTables() {
        transaction {
            exec("TRUNCATE users RESTART IDENTITY CASCADE;")
        }
    }
}
