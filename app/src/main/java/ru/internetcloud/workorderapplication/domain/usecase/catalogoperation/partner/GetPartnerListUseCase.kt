package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class GetPartnerListUseCase@Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend fun getPartnerList(): List<Partner> {
        return partnerRepository.getPartnerList()
    }
}
