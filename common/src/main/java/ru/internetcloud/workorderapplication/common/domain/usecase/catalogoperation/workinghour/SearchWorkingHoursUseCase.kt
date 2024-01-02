package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.workinghour

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.common.domain.repository.WorkingHourRepository

class SearchWorkingHoursUseCase @Inject constructor(
    private val workingHourRepository: WorkingHourRepository
) {
    suspend fun searchWorkingHours(searchText: String): List<WorkingHour> {
        return workingHourRepository.searchWorkingHours(searchText)
    }
}
