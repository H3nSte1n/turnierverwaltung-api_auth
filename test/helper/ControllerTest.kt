import helper.Controller
import io.mockk.every
import io.mockk.mockkObject
import io.mockk.verify
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import validation.UserValidation
import kotlin.test.assertEquals

class ControllerTest {

    @Test
    @DisplayName("Should call validateInput with each item in string List")
    fun integrationTestOnIsInputValid() {
        val inputs = arrayOf("foo", "bar", "foobar", "next", "hello")
        mockkObject(UserValidation)

        every { UserValidation.validateInput(String()) } returns true

        Controller.isInputValid(inputs)

        verify(exactly = 5) {
            UserValidation.validateInput(any())
        }
    }

    @Test
    @DisplayName("Should return true if all inputs are valid")
    fun testValidRun() {
        val inputs = arrayOf("foo", "bar", "foobar", "next", "hello")
        mockkObject(UserValidation)

        every { UserValidation.validateInput(String()) } returns true

        val isValid = Controller.isInputValid(inputs)

        assertEquals(true, isValid)
    }

    @Test
    @DisplayName("Should break up and return false is invalid")
    fun testInvalidRun() {
        val inputs = arrayOf("foo", "bar", "foobar", " ", "hello")
        mockkObject(UserValidation)

        every { UserValidation.validateInput(String()) } returns true

        val isValid = Controller.isInputValid(inputs)

        assertEquals(false, isValid)

        verify(exactly = 4) {
            UserValidation.validateInput(any())
        }
    }
}
