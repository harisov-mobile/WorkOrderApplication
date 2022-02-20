package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class DeleteAllPerformersUseCase(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllPerformers() {
        return synchroRepository.deleteAllPerformers()
    }
}
