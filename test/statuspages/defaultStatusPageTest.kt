import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals
import io.ktor.routing.*

class defaultStatusPageTest {

    @Test
    fun testResponse() {
        withTestApplication {
            application.install(StatusPages) {
                defaultStatusPage()
            }
            application.routing {
                get("/exception") {
                    throw ThrowableException()
                }
            }

            handleRequest(HttpMethod.Get, "/exception").let { call ->
                assertEquals(
                    HttpStatusCode.BadRequest,
                    call.response.status(),
                    "Should return status code 400"
                )
                assertEquals(
                    HttpStatusCode.BadRequest.description,
                    call.response.content,
                    "Should return error message from BadRequest"
                )
            }
        }
    }
}
