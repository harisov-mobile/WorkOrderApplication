package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class UploadWorkOrdersUseCase(private val synchroRepository: SynchroRepository) {
    suspend fun uploadWorkOrders(): FunctionResult {
        return synchroRepository.uploadWorkOrders()
    }
}
