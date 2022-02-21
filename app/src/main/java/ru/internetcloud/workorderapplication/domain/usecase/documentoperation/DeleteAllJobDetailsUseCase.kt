package ru.internetcloud.workorderapplication.domain.usecase.documentoperation

import ru.internetcloud.workorderapplication.domain.repository.SynchroRepository
import javax.inject.Inject

class DeleteAllJobDetailsUseCase @Inject constructor(private val synchroRepository: SynchroRepository) {

    suspend fun deleteAllJobDetails() {
        return synchroRepository.deleteAllJobDetails()
    }
}
