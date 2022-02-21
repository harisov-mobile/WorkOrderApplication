package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import ru.internetcloud.workorderapplication.domain.repository.WorkingHourRepository
import javax.inject.Inject

class AddWorkingHourListUseCase @Inject constructor(private val workingHourRepository: WorkingHourRepository) {

    suspend fun addWorkingHourList(workingHourList: List<WorkingHour>) {
        return workingHourRepository.addWorkingHourList(workingHourList)
    }
}
