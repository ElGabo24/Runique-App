package com.gapps.auth.domain

import assertk.assertThat
import assertk.assertions.isEqualTo
import assertk.assertions.isTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.CsvSource

class UserDataValidatorTest {

    private lateinit var userDataValidator: UserDataValidator

    @BeforeEach
    fun setUp() {
        userDataValidator = UserDataValidator(
            patternValidator = object : PatternValidator {
                override fun matches(value: String): Boolean {
                    return true
                }
            }
        )
    }

    @ParameterizedTest
    @CsvSource(
        "Test12345, true",
        "test12345, false",
        "345, false",
        "Roman101@!, true",
    )
    fun testValidatePassword(password: String, expected: Boolean) {
        val state = userDataValidator.validatePassword(password)
        assertThat(state.isValidPassword).isEqualTo(expected)
    }
}