package api.v1

import io.ktor.application.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.apiDoc() {
    get("/swagger") {
        call.respondRedirect("/swagger-ui/index.html?url=../static/core_1.0.0.yml", true)
    }
}
