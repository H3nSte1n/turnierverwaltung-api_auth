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
        val credentials = call.receive<User>()
        val token = LoginController.login(credentials)
        call.respond(token)
    }

    post("/sign-up") {
        val credentials = call.receive<User>()
        println(credentials)
        val token = RegisterController.register(credentials)
        call.respond(token)
    }
}
