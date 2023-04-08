package ru.internetcloud.workorderapplication.domain.usecase.synchrooperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class LoadMockDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun loadMockData() {
        return synchroRepository.loadMockData()
    }
}
