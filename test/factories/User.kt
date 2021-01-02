package factories

import com.turnierverwaltung_api_auth.enums.UserRole
import data.User
import helper.Password

object User {
    val instance by lazy {
        val generatedSalt = Password.generateSalt()
        User(
            null,
            "foo",
            "foo@bar.com",
            Password.generateHash("foo", generatedSalt),
            generatedSalt,
            UserRole.admin
        )
    }
}
