import io.ktor.routing.Routing
import io.ktor.routing.route

fun Routing.api() {
    route("/api/v1") {
        apiDoc()
    }
}
