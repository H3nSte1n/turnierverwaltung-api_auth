package statuspages

import com.turnierverwaltung_api_auth.statuspages.AuthenticationException
import com.turnierverwaltung_api_auth.statuspages.authStatusPage
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class AuthStatusPageTest {
    @Test
    fun testResponse() {
        withTestApplication {
            application.install(StatusPages) {
                authStatusPage()
            }
            application.routing {
                get("/exception") {
                    throw AuthenticationException()
                }
            }

            handleRequest(HttpMethod.Get, "/exception").let { call ->
                assertEquals(
                    HttpStatusCode.Unauthorized,
                    call.response.status(),
                    "Should return status code 401"
                )
                assertEquals(
                    AuthenticationException().message,
                    call.response.content,
                    "Should return custom error message"
                )
            }
        }
    }
}
