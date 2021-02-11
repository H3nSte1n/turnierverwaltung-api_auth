package data

import com.turnierverwaltung_api_auth.enums.UserRole
import io.ktor.auth.*
import org.jetbrains.exposed.dao.EntityID
import java.util.*

data class User(
    val id: EntityID<UUID>?,
    val name: String,
    val email: String?,
    val password: String,
    val salt: String?,
    val role: UserRole?
) : Principal {
    override fun toString(): String {
        return "$id $name $email $role"
    }
}
