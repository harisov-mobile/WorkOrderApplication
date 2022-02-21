package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class DeleteAllWorkOrdersUseCase @Inject constructor(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllWorkOrders() {
        return synchroRepository.deleteAllWorkOrders()
    }
}
