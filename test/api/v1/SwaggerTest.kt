package api.v1

import com.turnierverwaltung_api_auth.module
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class SwaggerTest {

    @Test
    fun swaggerRequests() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/swagger-ui/index.html?url=../static/core_1.0.0.yml#/")) {
            assertEquals(HttpStatusCode.OK, response.status())
            assertEquals("text", response.contentType().contentType)
        }
    }

    @Test
    fun testRedirectFromInterceptor() = withTestApplication(Application::module) {
        application.install(XForwardedHeaderSupport)
        with(handleRequest(HttpMethod.Get, "/api/v1/swagger")) {
            assertEquals(HttpStatusCode.MovedPermanently, response.status())
            assertEquals(null, response.content)
        }
    }
}
