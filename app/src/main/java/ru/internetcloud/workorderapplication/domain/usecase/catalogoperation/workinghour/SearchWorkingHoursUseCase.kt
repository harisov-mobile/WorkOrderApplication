package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository

class SearchWorkingHoursUseCase(private val workingHourRepository: WorkingHourRepository) {
    suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourRepository.searchWorkingHours(searchText)
    }
}
