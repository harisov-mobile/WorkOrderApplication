package ru.internetcloud.workorderapplication.login.domain.usecase

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.repository.AuthRepository

class SetAuthParametersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun setAuthParameters(server: String, login: String, password: String) {
        return authRepository.setAuthParameters(server, login, password)
    }
}
