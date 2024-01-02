package ru.internetcloud.workorderapplication.common.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.common.domain.repository.SynchroRepository

class SendWorkOrderToEmailUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult {
        return synchroRepository.sendWorkOrderToEmail(id, email)
    }
}
