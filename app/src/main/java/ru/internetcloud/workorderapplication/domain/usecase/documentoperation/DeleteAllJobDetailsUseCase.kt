package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository

class DeleteAllJobDetailsUseCase(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllJobDetails() {
        return synchroRepository.deleteAllJobDetails()
    }
}
