package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class AddPartnerListUseCase @Inject constructor(private val parnterRepository: PartnerRepository) {

    suspend fun addPartnerList(partnerList: List<Partner>) {
        return parnterRepository.addPartnerList(partnerList)
    }
}
