package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.common.UpdateState
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class UpdateDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun updateData(): UpdateState {
        return synchroRepository.updateData()
    }
}
