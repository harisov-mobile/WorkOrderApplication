package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.FunctionResult
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class UploadWorkOrdersUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {

    suspend fun uploadWorkOrders(): FunctionResult {
        return synchroRepository.uploadWorkOrders()
    }
}
