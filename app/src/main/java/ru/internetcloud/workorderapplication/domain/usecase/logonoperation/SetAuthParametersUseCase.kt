package ru.internetcloud.workorderapplication.domain.usecase.logonoperation

import ru.internetcloud.workorderapplication.domain.repository.AuthRepository
import javax.inject.Inject

class SetAuthParametersUseCase @Inject constructor(private val authRepository: AuthRepository) {

    fun setAuthParameters(server: String, login: String, password: String) {
        return authRepository.setAuthParameters(server, login, password)
    }
}
