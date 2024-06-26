package ru.internetcloud.workorderapplication.common.domain.usecase.synchrooperation

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.repository.SynchroRepository


class LoadMockDataUseCase @Inject constructor(
    private val synchroRepository: SynchroRepository
) {
    suspend fun loadMockData() {
        return synchroRepository.loadMockData()
    }
}
