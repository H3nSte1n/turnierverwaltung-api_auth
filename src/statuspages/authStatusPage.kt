package statuspages
import io.ktor.application.*
import io.ktor.features.*
import io.ktor.http.*
import io.ktor.response.*

fun StatusPages.Configuration.authStatusPage() {
    exception<AuthenticationException> { cause ->
        call.respondText(
            cause.message,
            ContentType.Text.Plain,
            status = HttpStatusCode.Unauthorized
        )
    }
}

data class AuthenticationException(override val message: String = "Authentication failed") : Exception()
