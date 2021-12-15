package ru.internetcloud.workorderapplication.domain.usecase.logonoperation

import ru.internetcloud.workorderapplication.domain.common.AuthParameters
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository

class GetAuthParametersUseCase(private val authRepository: AuthRepository) {

    fun getAuthParams(): AuthParameters {
        return authRepository.getAuthParameters()
    }
}
