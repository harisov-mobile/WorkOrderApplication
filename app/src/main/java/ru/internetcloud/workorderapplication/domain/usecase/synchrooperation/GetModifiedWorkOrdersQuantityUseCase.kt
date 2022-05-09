package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class GetModifiedWorkOrdersQuantityUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {

    suspend fun getModifiedWorkOrdersQuantity(): Int {
        return synchroRepository.getModifiedWorkOrdersQuantity()
    }
}
