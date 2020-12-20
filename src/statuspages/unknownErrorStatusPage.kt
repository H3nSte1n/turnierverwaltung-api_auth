package statuspages

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.unknownErrorStatusPage() {
    exception<UnknownErrorException> { cause ->
        call.respondText(
            cause.message,
            ContentType.Text.Plain,
            status = HttpStatusCode.InternalServerError
        )
    }
}

data class UnknownErrorException(override val message: String = HttpStatusCode.InternalServerError.description) : Exception()
