package statuspages

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.defaultStatusPage() {
    exception<ThrowableException> { cause ->
        call.respondText(
            cause.message,
            ContentType.Text.Plain,
            status = HttpStatusCode.BadRequest
        )
    }
}

data class ThrowableException(override val message: String = HttpStatusCode.BadRequest.description) : Exception()
