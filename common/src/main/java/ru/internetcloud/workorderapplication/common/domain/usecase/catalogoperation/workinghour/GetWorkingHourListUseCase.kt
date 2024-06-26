package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.workinghour

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour
import ru.internetcloud.workorderapplication.common.domain.repository.WorkingHourRepository

class GetWorkingHourListUseCase @Inject constructor(
    private val workingHourRepository: WorkingHourRepository
) {
    suspend fun getWorkingHourList(): List<WorkingHour> {
        return workingHourRepository.getWorkingHourList()
    }
}
