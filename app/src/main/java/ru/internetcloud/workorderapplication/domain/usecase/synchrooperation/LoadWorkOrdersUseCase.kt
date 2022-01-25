package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class LoadWorkOrdersUseCase(private val synchroRepository: SynchroRepository) {
    suspend fun loadWorkOrders(): Boolean {
        return synchroRepository.loadWorkOrders()
    }
}
