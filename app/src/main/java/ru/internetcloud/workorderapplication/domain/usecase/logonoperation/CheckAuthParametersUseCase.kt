package ru.internetcloud.workorderapplication.domain.usecase.logonoperation

import ru.internetcloud.workorderapplication.domain.common.AuthResult
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository
import javax.inject.Inject

class CheckAuthParametersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun checkAuthorization(): AuthResult {
        return authRepository.checkAuthorization()
    }
}
