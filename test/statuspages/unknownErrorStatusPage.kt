import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class unknownErrorStatusPage {

    @Test
    fun testResponse() {
        withTestApplication {
            application.install(StatusPages) {
                unknownErrorStatusPage()
            }
            application.routing {
                get("/exception") {
                    throw UnknownErrorException()
                }
            }

            handleRequest(HttpMethod.Get, "/exception").let { call ->
                assertEquals(
                    HttpStatusCode.InternalServerError,
                    call.response.status(),
                    "Should return status code 500"
                )
                assertEquals(
                    HttpStatusCode.InternalServerError.description,
                    call.response.content,
                    "Should return error message from InternalServerError"
                )
            }
        }
    }
}
