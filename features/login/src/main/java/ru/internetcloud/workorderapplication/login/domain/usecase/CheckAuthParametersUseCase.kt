package ru.internetcloud.workorderapplication.login.domain.usecase

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.AuthResult
import ru.internetcloud.workorderapplication.common.domain.repository.AuthRepository

class CheckAuthParametersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend fun checkAuthorization(): AuthResult {
        return authRepository.checkAuthorization()
    }
}
