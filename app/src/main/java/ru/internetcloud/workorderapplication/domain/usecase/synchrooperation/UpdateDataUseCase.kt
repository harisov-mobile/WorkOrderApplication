package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import ru.internetcloud.workorderapplication.presentation.synchro.UpdateState

class UpdateDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun updateData(): UpdateState {
        return synchroRepository.updateData()
    }
}
