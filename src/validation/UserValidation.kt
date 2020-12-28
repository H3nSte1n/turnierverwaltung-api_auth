package validation

import data.User
import helper.Password.generateHash
import schemas.Users

object UserValidation {
    fun validateLoginCredentials(user: User, password: String): Boolean {
        val passwordIsValid = validatePassword(
            password,
            user.salt!!,
            user.password
        )
        if (!passwordIsValid) return false

        return true
    }

    fun validateUserExist(name: String): Boolean {
        if (Users.userExist(name)) return true

        return false
    }

    fun validateInput(input: String): Boolean {
        if (input.isEmpty()) return false
        if (input.isBlank()) return false

        return true
    }

    fun validatePassword(password: String, salt: String, expectedPwd: String): Boolean {
        val checkHash = generateHash(password, salt)

        if (checkHash.length != expectedPwd.length) return false

        for ((index, char) in checkHash.withIndex()) {
            if (char != expectedPwd[index]) return false
        }

        return true
    }
}
