package ru.internetcloud.workorderapplication.common.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.common.domain.repository.SynchroRepository

class UploadWorkOrderByIdUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun uploadWorkOrderById(id: String): FunctionResult {
        return synchroRepository.uploadWorkOrderById(id)
    }
}
