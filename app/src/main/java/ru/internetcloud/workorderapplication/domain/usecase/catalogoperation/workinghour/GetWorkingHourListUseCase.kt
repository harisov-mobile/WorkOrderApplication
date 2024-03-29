package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class GetWorkingHourListUseCase @Inject constructor(
    private val workingHourRepository: WorkingHourRepository
) {
    suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourRepository.getWorkingHourList()
    }
}
