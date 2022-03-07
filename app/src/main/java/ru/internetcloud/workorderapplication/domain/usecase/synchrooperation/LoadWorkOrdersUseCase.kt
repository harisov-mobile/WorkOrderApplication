package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class LoadWorkOrdersUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {

    suspend fun loadWorkOrders(): Boolean {
        return synchroRepository.loadWorkOrders()
    }
}
