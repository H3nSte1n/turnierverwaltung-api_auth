package helper

import factories.User
import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class JwtTest {

    @Test
    fun testGenerateAndReturnToken() {
        val user = User.instance

        val token = Jwt.generateToken(user)

        assertNotNull(token)
    }

}
