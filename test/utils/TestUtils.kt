import io.github.cdimascio.dotenv.dotenv

object TestUtils {
    val env by lazy {
        dotenv {
            filename = ".env.test"
            ignoreIfMissing = true
        }
    }
}
