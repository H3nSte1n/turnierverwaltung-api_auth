package controller

import factories.User
import helper.Controller
import helper.Jwt
import io.mockk.*
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import schemas.Users
import statuspages.AuthenticationException
import statuspages.ThrowableException
import validation.UserValidation
import kotlin.test.BeforeTest
import kotlin.test.assertEquals

class LoginControllerTest {

    lateinit var user: data.User

    @BeforeTest
    fun prepare() {
        user = User.instance
        unmockkAll()
    }

    @AfterEach
    fun afterTest() {
        unmockkAll()
    }

    @Nested
    inner class when_run_login {

        @Test
        fun should_call_specific_methods() {
            mockkObject(Controller)
            mockkObject(Users)
            mockkObject(UserValidation)
            mockkObject(Jwt)

            every { Controller.isInputValid(any()) } returns true
            every { UserValidation.validateUserExist(any()) } returns true
            every { Users.findUser(any()) } returns user
            every { UserValidation.validateLoginCredentials(any(), any()) } returns true
            every { Jwt.generateToken(any()) } returns "asd.asd.asd"

            LoginController.login(user)

            verify {
                Controller.isInputValid(any())
                UserValidation.validateUserExist(any())
                Users.findUser(any())
                UserValidation.validateLoginCredentials(any(), any())
                Jwt.generateToken(any())
            }

            verifyOrder {
                Controller.isInputValid(any())
                UserValidation.validateUserExist(any())
                Users.findUser(any())
                UserValidation.validateLoginCredentials(any(), any())
                Jwt.generateToken(any())
            }
        }

        @Test
        fun should_break_up_if_input_is_invalid() {
            mockkObject(Controller)

            every { Controller.isInputValid(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                LoginController.login(user)
            }
        }

        @Test
        fun should_break_up_if_user_not_exist() {
            mockkObject(Controller)
            mockkObject(UserValidation)

            every { Controller.isInputValid(any()) } returns true
            every { UserValidation.validateUserExist(any()) } returns false

            assertThrows(ThrowableException::class.java) {
                LoginController.login(user)
            }
        }

        @Test
        fun should_break_up_if_authentication_failed() {
            mockkObject(Controller)
            mockkObject(Users)
            mockkObject(UserValidation)

            every { Controller.isInputValid(any()) } returns true
            every { UserValidation.validateUserExist(any()) } returns true
            every { Users.findUser(any()) } returns user
            every { UserValidation.validateLoginCredentials(any(), any()) } returns false

            assertThrows(AuthenticationException::class.java) {
                LoginController.login(user)
            }
        }
    }
}
