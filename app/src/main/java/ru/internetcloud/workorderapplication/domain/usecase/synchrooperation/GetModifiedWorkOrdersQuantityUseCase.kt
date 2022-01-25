package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class GetModifiedWorkOrdersQuantityUseCase(private val synchroRepository: SynchroRepository) {
    suspend fun getModifiedWorkOrdersQuantity(): Int {
        return synchroRepository.getModifiedWorkOrdersQuantity()
    }
}
