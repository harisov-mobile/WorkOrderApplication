package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class DeleteAllWorkOrdersUseCase(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllWorkOrders() {
        return synchroRepository.deleteAllWorkOrders()
    }
}
