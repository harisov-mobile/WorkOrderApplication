package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.common.domain.common.AuthResult

interface AuthRepository {

    fun getAuthParameters(): AuthParameters

    fun setAuthParameters(server: String, login: String, password: String)

    suspend fun checkAuthorization(): AuthResult
}
