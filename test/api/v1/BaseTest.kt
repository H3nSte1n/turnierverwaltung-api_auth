import api.api
import api.v1.v1
import com.fasterxml.jackson.databind.SerializationFeature
import controller.LoginController
import factories.User
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.Test
import statuspages.AuthenticationException
import statuspages.authStatusPage
import kotlin.test.assertEquals

class BaseTest: DatabaseConnection() {

    @Test
    fun call_v1_method() {
        mockkObject(LoginController)
        every { LoginController.login(any()) } returns "xxx.xxx.xxx"

        withTestApplication {
            application.routing {
                api()
            }

            application.install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            handleRequest(HttpMethod.Post, "/api/v1/sign-in") {
                addHeader("Accept", "text/plain")
                addHeader("Content-Type", "application/json; charset=UTF-16")
                setBody("""{"name":"Henry","password":"foobar"}""".toByteArray(charset = Charsets.UTF_16))
            }.let { call ->
                verify {
                    LoginController.login(any())
                }
            }
        }
    }
}
