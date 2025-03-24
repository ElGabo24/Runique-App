package com.gapps.auth.domain

class UserDataValidator(
    private val patternValidator: PatternValidator
) {

    fun isValidEmial(email: String): Boolean {
        return patternValidator.matches(email.trim())
    }

    fun validatePassword(password: String): PasswordValidationState {
        val hasLenght = password.length >= MIN_PASSWORD_LENGHT
        val hasDigit = password.any{ it.isDigit() }
        val hasLowerCaseCharacter = password.any { it.isLowerCase() }
        val hasUpperCaseCharacter = password.any { it.isUpperCase() }

        return PasswordValidationState(
            hasNumber = hasDigit,
            hasMinLength = hasLenght,
            hasLowerCaseCharacter = hasLowerCaseCharacter,
            hasUpperCaseCharacter = hasUpperCaseCharacter
        )
    }

    companion object {
        const val MIN_PASSWORD_LENGHT = 9
    }

}