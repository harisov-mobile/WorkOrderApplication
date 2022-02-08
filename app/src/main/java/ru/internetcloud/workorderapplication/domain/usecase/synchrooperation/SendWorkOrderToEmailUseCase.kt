package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class SendWorkOrderToEmailUseCase(private val synchroRepository: SynchroRepository) {

    suspend fun sendWorkOrderToEmail(id: String): FunctionResult {
        return synchroRepository.sendWorkOrderToEmail(id)
    }
}
