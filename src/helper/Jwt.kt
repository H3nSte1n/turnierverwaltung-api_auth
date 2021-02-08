package helper

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import data.User
import io.github.cdimascio.dotenv.dotenv
import java.util.*

object Jwt {
    private val dotenv = dotenv {
        ignoreIfMissing = true
    }
    private const val MIN = 3_600_000
    private val issuer = dotenv["ISSUER"]
    private val secret = dotenv["SECRET"]
    private val algorithm = Algorithm.HMAC512(secret)

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withExpiresAt(getExpirationDate(10))
        .withClaim("name", user.name)
        .withClaim("role", user.role.toString())
        .sign(algorithm)

    fun verifier(): JWTVerifier = JWT
        .require(algorithm)
        .withIssuer(issuer)
        .build()

    private fun getExpirationDate(hours: Int) =
        Date(System.currentTimeMillis() + (MIN * hours))
}
