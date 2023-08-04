package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class SearchWorkingHoursUseCase @Inject constructor(
    private val workingHourRepository: WorkingHourRepository
) {
    suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourRepository.searchWorkingHours(searchText)
    }
}
