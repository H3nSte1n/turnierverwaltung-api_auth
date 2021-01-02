package api.v1

import api.api
import com.fasterxml.jackson.databind.SerializationFeature
import com.papsign.ktor.openapigen.OpenAPIGen
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.*

class SwaggerTest {

    @Nested
    inner class api_v1_swagger {

        @Test
        fun should_redirect_from_interceptor() {
            val path = "/api/v1/swagger"

            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.MovedPermanently, call.response.status())
                assertEquals(null, call.response.content)
            }
            initApplication(::tests, path)
        }

        @Test
        fun should_response_swagger_doku() {
            val path = "/swagger-ui/index.html?url=../static/core_1.0.0.yml#/"

            fun tests(call: TestApplicationCall) {
                assertEquals(HttpStatusCode.OK, call.response.status())
                assertEquals("text", call.response.contentType().contentType)
            }
            initApplication(::tests, path)
        }
    }

    private fun initApplication(tests: (call: TestApplicationCall) -> Unit, path: String) {
        withTestApplication {

            application.install(OpenAPIGen) {
                serveSwaggerUi = true
                swaggerUiPath = "/swagger-ui"
            }

            application.routing {
                static("/static") {
                    resources("api")
                }

                api()
            }

            application.install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            handleRequest(HttpMethod.Get, path) {
            }.let { call ->
                tests(call)
            }
        }
    }
}
