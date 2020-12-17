package io.ktor.samples.testable

import com.turnierverwaltung_api_auth.module
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.server.testing.*
import kotlin.test.*

class SwaggerTest {

    @Test
    fun swaggerRequests() = withTestApplication(Application::module) {
        with(handleRequest(HttpMethod.Get, "/swagger-ui/index.html?url=../static/core_1.0.0.yml#/")) {
            assertEquals(HttpStatusCode.OK, response.status())
        }
    }
}
