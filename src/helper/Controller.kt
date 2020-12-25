package helper

import validation.UserValidation

object Controller {
    fun isInputValid(methods: Array<String>): Boolean {
        for(input in methods) {
            if(!UserValidation.validateInput(input)) return false
        }

        return true
    }
}
