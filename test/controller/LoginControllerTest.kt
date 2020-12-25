package controller

import factories.User
import helper.Controller
import helper.Jwt
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import schemas.Users
import statuspages.AuthenticationException
import statuspages.ThrowableException
import validation.UserValidation

class LoginControllerTest {

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Test
    @DisplayName("Integration Test login()")
    fun integrationTestsOnLogin() {
        val user = User.instance

        mockkObject(Controller)
        mockkObject(Users)
        mockkObject(UserValidation)
        mockkObject(Jwt)

        every { Controller.isInputValid(any()) } returns true
        every { Users.findUser(any()) } returns user
        every { UserValidation.validateLoginCredentials(any(), any()) } returns true
        every { Jwt.generateToken(any()) } returns "asd.asd.asd"

        LoginController.login(user)

        verify {
            Controller.isInputValid(any())
            Users.findUser(any())
            UserValidation.validateLoginCredentials(any(), any())
            Jwt.generateToken(any())
        }

        verifyOrder {
            Controller.isInputValid(any())
            Users.findUser(any())
            UserValidation.validateLoginCredentials(any(), any())
            Jwt.generateToken(any())
        }
    }

    @Test
    fun breakUpIfInputIsInvalid() {
        val user = User.instance

        mockkObject(Controller)
        mockkObject(Users)
        mockkObject(UserValidation)
        mockkObject(Jwt)

        every { Controller.isInputValid(any()) } returns false

        assertThrows(ThrowableException::class.java) {
            LoginController.login(user)
        }
    }

    @Test
    fun breakUpIfAuthenticationFailed() {
        val user = User.instance

        mockkObject(Controller)
        mockkObject(Users)
        mockkObject(UserValidation)
        mockkObject(Jwt)

        every { Controller.isInputValid(any()) } returns true
        every { Users.findUser(any()) } returns user
        every { UserValidation.validateLoginCredentials(any(), any()) } returns false

        assertThrows(AuthenticationException::class.java) {
            LoginController.login(user)
        }
    }
}
