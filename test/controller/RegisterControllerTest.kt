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
import statuspages.ThrowableException
import validation.UserValidation

class RegisterControllerTest {

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Test
    @DisplayName("Integration Test login()")
    fun integrationTestsOnRegister() {
        val user = User.instance

        mockkObject(Controller)
        mockkObject(UserValidation)
        mockkObject(Users)
        mockkObject(Jwt)

        every { Controller.isInputValid(any()) } returns true
        every { UserValidation.validateUserExist(any()) } returns false
        every { Users.createUser(any()) } returns user
        every { Jwt.generateToken(any()) } returns "asd.asd.asd"

        RegisterController.register(user)

        verify {
            Controller.isInputValid(any())
            UserValidation.validateUserExist(any())
            Users.createUser(any())
            Jwt.generateToken(any())
        }

        verifyOrder {
            Controller.isInputValid(any())
            UserValidation.validateUserExist(any())
            Users.createUser(any())
            Jwt.generateToken(any())
        }
    }

    @Test
    fun breakUpIfInputIsInvalid() {
        val user = User.instance

        mockkObject(Controller)

        every { Controller.isInputValid(any()) } returns false

        assertThrows(ThrowableException::class.java) {
            RegisterController.register(user)
        }
    }

    /*@Test
    fun breakUpIfUserExist() {
        val user = User.instance

        mockkObject(Controller)
        mockkObject(UserValidation)

        every { Controller.isInputValid(any()) } returns false
        every { UserValidation.validateUserExist(any()) } returns true

        assertThrows(ThrowableException::class.java) {
            RegisterController.register(user)
        }
    }*/
}
