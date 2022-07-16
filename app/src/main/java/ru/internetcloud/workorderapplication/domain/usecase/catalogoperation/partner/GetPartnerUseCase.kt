package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class GetPartnerUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend fun getPartner(id: String): Partner? {
        return partnerRepository.getPartner(id)
    }
}
