package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class DeleteAllPerformersUseCase @Inject constructor(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllPerformers() {
        return synchroRepository.deleteAllPerformers()
    }
}
