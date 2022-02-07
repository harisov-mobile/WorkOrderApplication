package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.domain.common.AuthResult

interface AuthRepository {

    fun getAuthParameters(): AuthParameters

    fun setAuthParameters(server: String, login: String, password: String)

    suspend fun checkAuthorization(): AuthResult
}
