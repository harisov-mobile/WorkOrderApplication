package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

class AddWorkingHourListUseCase(private val workingHourRepository: WorkingHourRepository) {
    suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        return workingHourRepository.addWorkingHourList(workingHourList)
    }
}
