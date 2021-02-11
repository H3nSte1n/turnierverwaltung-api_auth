package data

import com.turnierverwaltung_api_auth.enums.UserRole

data class Credentials(
    val token: String,
    val role: UserRole?,
)
