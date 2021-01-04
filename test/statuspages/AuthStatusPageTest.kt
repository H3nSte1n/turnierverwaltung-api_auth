package statuspages

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class AuthStatusPageTest {

    @Nested
    inner class when_throw_exception {

        @Test
        fun response_status_and_message() {
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
}
