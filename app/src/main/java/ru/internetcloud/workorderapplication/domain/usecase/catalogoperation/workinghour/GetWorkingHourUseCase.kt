package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.workinghour

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.repository.CarJobRepository

class GetWorkingHourUseCase @Inject constructor(
    private val carJobRepository: CarJobRepository
) {
}
