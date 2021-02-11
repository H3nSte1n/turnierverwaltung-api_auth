package com.turnierverwaltung_api_auth

import api.api
import com.fasterxml.jackson.databind.*
import com.papsign.ktor.openapigen.OpenAPIGen
import config.DatabaseFactory
import helper.Jwt
import io.github.cdimascio.dotenv.dotenv
import io.ktor.application.*
import io.ktor.auth.*
import io.ktor.auth.jwt.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.response.*
import io.ktor.routing.*
import statuspages.authStatusPage
import statuspages.defaultStatusPage
import statuspages.invalidUserStatusPage
import statuspages.unknownErrorStatusPage
import javax.annotation.processing.Generated

@Generated
fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.main() {
    val dotenv = dotenv {
        ignoreIfMissing = true
    }
    DatabaseFactory.init(dotenv)

    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        method(HttpMethod.Post)
        method(HttpMethod.Head)
        allowNonSimpleContentTypes = true
        allowCredentials = false
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
    }

    install(Authentication) {
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

    install(StatusPages) {
        authStatusPage()
        invalidUserStatusPage()
        defaultStatusPage()
        unknownErrorStatusPage()
    }

    install(ContentNegotiation) {
        jackson {
            enable(SerializationFeature.INDENT_OUTPUT)
        }
    }

    install(OpenAPIGen) {
        serveSwaggerUi = true
        swaggerUiPath = "/swagger-ui"
    }

    routing {
        static("/static") {
            resources("api")
        }

        api()
    }
}
