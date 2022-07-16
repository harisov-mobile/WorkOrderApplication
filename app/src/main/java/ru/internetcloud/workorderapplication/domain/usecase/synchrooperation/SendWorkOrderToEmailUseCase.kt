package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class SendWorkOrderToEmailUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult {
        return synchroRepository.sendWorkOrderToEmail(id, email)
    }
}
