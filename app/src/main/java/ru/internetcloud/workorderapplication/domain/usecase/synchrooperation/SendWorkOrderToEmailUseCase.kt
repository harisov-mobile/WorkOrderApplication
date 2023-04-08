package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class SendWorkOrderToEmailUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun sendWorkOrderToEmail(id: String, email: String): FunctionResult {
        return synchroRepository.sendWorkOrderToEmail(id, email)
    }
}
