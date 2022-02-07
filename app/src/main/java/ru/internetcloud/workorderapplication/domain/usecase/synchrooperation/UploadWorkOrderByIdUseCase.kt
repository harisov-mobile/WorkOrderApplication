package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class UploadWorkOrderByIdUseCase(private val synchroRepository: SynchroRepository) {

    suspend fun uploadWorkOrderById(id: String): FunctionResult {
        return synchroRepository.uploadWorkOrderById(id)
    }
}
