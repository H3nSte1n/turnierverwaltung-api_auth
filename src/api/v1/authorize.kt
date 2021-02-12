package api.v1

import io.ktor.auth.*
import io.ktor.routing.*

fun Route.authorize() {
    authenticate {
        post("/auth") {}
    }
}
