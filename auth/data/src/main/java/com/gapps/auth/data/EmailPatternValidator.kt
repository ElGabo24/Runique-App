package com.gapps.auth.data

import android.util.Patterns
import com.gapps.auth.domain.PatternValidator

class EmailPatternValidator: PatternValidator {

    override fun matches(value: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(value).matches()
    }

}