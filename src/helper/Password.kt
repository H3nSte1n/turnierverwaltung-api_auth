package helper

import org.mindrot.jbcrypt.BCrypt

object Password {
    fun generateHash(password: String, salt: String): String {
        return BCrypt.hashpw(password, salt)
    }

    fun generateSalt(): String {
        return BCrypt.gensalt(16)
    }
}
