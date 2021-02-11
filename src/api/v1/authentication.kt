package api.v1

import controller.LoginController
import controller.RegisterController
import data.User
import io.ktor.application.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun Route.authentication() {
    post("/sign-in") {
        val receivedCredentials = call.receive<User>()
        val respondedCredentials = LoginController.login(receivedCredentials)
        call.respond(respondedCredentials)
    }

    post("/sign-up") {
        val receivedCredentials = call.receive<User>()
        val respondedCredentials = RegisterController.register(receivedCredentials)
        call.respond(respondedCredentials)
    }
}
