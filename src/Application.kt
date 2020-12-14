package com.turnierverwaltung_api_auth

import api
import com.fasterxml.jackson.databind.*
import com.papsign.ktor.openapigen.OpenAPIGen
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.jackson.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

@kotlin.jvm.JvmOverloads
fun Application.module() {
    install(CORS) {
        method(HttpMethod.Options)
        method(HttpMethod.Put)
        method(HttpMethod.Delete)
        method(HttpMethod.Patch)
        header(HttpHeaders.Authorization)
        header("MyCustomHeader")
        allowCredentials = true
        anyHost() // @TODO: Don't do this in production if possible. Try to limit it.
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

data class JsonSampleClass(val hello: String)
