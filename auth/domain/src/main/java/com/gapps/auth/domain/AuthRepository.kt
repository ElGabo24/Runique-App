package com.gapps.auth.domain

import com.gapps.core.domain.util.DataError
import com.gapps.core.domain.util.EmptyResult

interface AuthRepository {

    suspend fun login(email: String, password: String): EmptyResult<DataError.Network>
    suspend fun register(email: String, password: String): EmptyResult<DataError.Network>

}