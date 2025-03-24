package com.gapps.auth.domain

interface PatternValidator {
    fun matches(value: String): Boolean
}