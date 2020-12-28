package helper

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.typesafe.config.ConfigFactory
import data.User
import java.util.*

object Jwt {
    private const val MIN = 3_600_000
    private val issuer = ConfigFactory.load().getString("jwt.ISSUER")
    private val secret = ConfigFactory.load().getString("jwt.SECRET")
    private val algorithm = Algorithm.HMAC512(secret)

    fun generateToken(user: User): String = JWT.create()
        .withSubject("Authentication")
        .withIssuer(issuer)
        .withExpiresAt(getExpirationDate(10))
        .withClaim("name", user.name)
        .withClaim("role", user.role.toString())
        .sign(algorithm)

    private fun getExpirationDate(hours: Int) =
        Date(System.currentTimeMillis() + (MIN * hours))
}
