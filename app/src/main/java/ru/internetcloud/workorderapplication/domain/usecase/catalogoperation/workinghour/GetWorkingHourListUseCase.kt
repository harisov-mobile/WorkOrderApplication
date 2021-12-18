package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

class GetWorkingHourListUseCase(private val workingHourRepository: WorkingHourRepository) {
    suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourRepository.getWorkingHourList()
    }
}
