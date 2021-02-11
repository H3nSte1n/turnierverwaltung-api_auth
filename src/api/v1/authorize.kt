package api.v1

import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.authorize() {
    authenticate {
        post("/auth") {}
    }
}
