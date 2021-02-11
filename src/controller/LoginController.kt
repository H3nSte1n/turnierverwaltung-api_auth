package controller

import data.Credentials
import data.User
import helper.Controller.isInputValid
import helper.Jwt
import schemas.Users
import statuspages.AuthenticationException
import statuspages.InvalidUserException
import statuspages.ThrowableException
import validation.UserValidation.validateLoginCredentials
import validation.UserValidation.validateUserExist

object LoginController {
    fun login(credentials: User): Credentials {
        val inputs = arrayOf(credentials.name, credentials.password)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (!validateUserExist(credentials.name)) throw InvalidUserException()
        val user = Users.findUser(credentials.name)
        if (!validateLoginCredentials(user, credentials.password)) throw AuthenticationException()
        val token = Jwt.generateToken(user)

        return Credentials(token, user.role)
    }
}
