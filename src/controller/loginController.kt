package controller

import data.User
import helper.Controller.isInputValid
import helper.Jwt
import schemas.Users
import statuspages.AuthenticationException
import statuspages.ThrowableException
import validation.UserValidation.validateLoginCredentials

object LoginController {
    fun login(credentials: User): String {
        val inputs = arrayOf(credentials.name, credentials.password)

        if(!isInputValid(inputs)) throw ThrowableException()
        val user = Users.findUser(credentials.name)
        if(!validateLoginCredentials(user, credentials.password)) throw AuthenticationException()

        return Jwt.generateToken(user)
    }
}
