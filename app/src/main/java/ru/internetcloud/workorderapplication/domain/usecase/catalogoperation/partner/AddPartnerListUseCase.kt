package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class AddPartnerListUseCase(private val carJobRepository: PartnerRepository) {
    suspend fun addPartnerList(carJobList: List<Partner>) {
        return carJobRepository.addPartnerList(carJobList)
    }
}
