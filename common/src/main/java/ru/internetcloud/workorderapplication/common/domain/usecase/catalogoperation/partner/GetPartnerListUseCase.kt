package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.partner

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.common.domain.repository.PartnerRepository

class GetPartnerListUseCase@Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend fun getPartnerList(): List<Partner> {
        return partnerRepository.getPartnerList()
    }
}
