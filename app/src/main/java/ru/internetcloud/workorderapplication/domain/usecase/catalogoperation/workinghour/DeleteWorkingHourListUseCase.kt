package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class DeleteWorkingHourListUseCase @Inject constructor(private val workingHourRepository: WorkingHourRepository) {
    suspend fun deleteAllWorkingHours() {
        return workingHourRepository.deleteAllWorkingHours()
    }
}
