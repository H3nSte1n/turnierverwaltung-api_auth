import api.api
import com.fasterxml.jackson.databind.SerializationFeature
import helper.Jwt
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import io.ktor.server.testing.*
import io.ktor.util.*
import io.ktor.utils.io.charsets.*
import io.netty.handler.codec.http.HttpHeaders.addHeader
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals
import kotlin.text.Charsets

class AuthorizeTest {
    @Nested
    inner class api_v1_auth {
        val path = "/api/v1/auth"

        @Nested
        inner class with_valid_jwt_token {
            lateinit var encoded: String
            val user = factories.User.instance

            @InternalAPI
            @BeforeTest
            fun prepare() {
                encoded = Jwt.generateToken(user)
            }

            @Test
            fun should_respond_admin_user_role() {
                fun tests(call: TestApplicationCall) {
                    assertEquals(user.role.toString(), call.response.content)
                }
                initApplication(::tests, path, "", encoded)
            }

            @Test
            fun should_return_200_http_status_code() {
                fun tests(call: TestApplicationCall) {
                    assertEquals(HttpStatusCode.OK, call.response.status())
                }
                initApplication(::tests, path, "", encoded)
            }
        }

        @Nested
        inner class with_invalid_jwt_token {

            @Test
            fun should_return_401_http_status_code() {
                fun tests(call: TestApplicationCall) {
                    assertEquals(HttpStatusCode.Unauthorized, call.response.status())
                }
                initApplication(::tests, path, "", "")
            }
        }
    }

    private fun initApplication(tests: (call: TestApplicationCall) -> Unit, path: String, body: String, encoded: String) {
        withTestApplication {
            application.install(Authentication) {
                jwt {
                    verifier(Jwt.verifier())
                    validate {
                        val name = it.payload.getClaim("name").asString()
                        val role = it.payload.getClaim("role").asString()
                        if (name !== null) {
                            respondText(role)
                            JWTPrincipal(it.payload)
                        } else {
                            null
                        }
                    }
                }
            }

            application.routing {
                api()
            }

            application.install(ContentNegotiation) {
                jackson {
                    enable(SerializationFeature.INDENT_OUTPUT)
                }
            }

            handleRequest(HttpMethod.Post, path) {
                addHeader("Accept", "*")
                addHeader("Content-Type", "application/json; charset=UTF-16")
                addHeader(HttpHeaders.Authorization, "Bearer $encoded")
                setBody(body.toByteArray(charset = Charsets.UTF_16))
            }.let { call ->
                tests(call)
            }
        }
    }
}
