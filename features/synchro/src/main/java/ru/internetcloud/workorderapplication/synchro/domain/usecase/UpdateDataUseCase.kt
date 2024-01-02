package ru.internetcloud.workorderapplication.synchro.domain.usecase

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.UpdateState
import ru.internetcloud.workorderapplication.common.domain.repository.SynchroRepository

class UpdateDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun updateData(): UpdateState {
        return synchroRepository.updateData()
    }
}
