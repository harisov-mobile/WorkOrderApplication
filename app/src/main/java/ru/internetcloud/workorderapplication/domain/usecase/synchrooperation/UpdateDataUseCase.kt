package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.common.UpdateState
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class UpdateDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun updateData(): UpdateState {
        return synchroRepository.updateData()
    }
}
