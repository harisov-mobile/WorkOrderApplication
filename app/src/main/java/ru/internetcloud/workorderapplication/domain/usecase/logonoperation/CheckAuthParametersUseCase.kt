package ru.internetcloud.workorderapplication.domain.usecase.logonoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.common.AuthResult
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository

class CheckAuthParametersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun checkAuthorization(): AuthResult {
        return authRepository.checkAuthorization()
    }
}
