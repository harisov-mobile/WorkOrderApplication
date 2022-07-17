package ru.internetcloud.workorderapplication.domain.usecase.logonoperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.repository.AuthRepository

class SetAuthParametersUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    fun setAuthParameters(server: String, login: String, password: String) {
        return authRepository.setAuthParameters(server, login, password)
    }
}
