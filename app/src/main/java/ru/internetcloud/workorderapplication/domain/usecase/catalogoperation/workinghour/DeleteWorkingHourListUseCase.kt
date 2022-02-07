package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

class DeleteWorkingHourListUseCase(private val workingHourRepository: WorkingHourRepository) {
    suspend fun deleteAllWorkingHours() {
        return workingHourRepository.deleteAllWorkingHours()
    }
}
