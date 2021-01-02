package controller

import data.User
import helper.Controller.isInputValid
import helper.Jwt
import schemas.Users.createUser
import statuspages.InvalidUserException
import statuspages.ThrowableException
import validation.UserValidation.validateUserExist

object RegisterController {
    fun register(credentials: User): String {
        val inputs = arrayOf(credentials.name, credentials.password, credentials.role!!.name)

        if (!isInputValid(inputs)) throw ThrowableException()
        if (validateUserExist(credentials.name)) throw InvalidUserException()
        val user: User = createUser(credentials)

        return Jwt.generateToken(user)
    }
}
