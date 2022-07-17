package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class SearchPartnersUseCase @Inject constructor(
    private val partnerRepository: PartnerRepository
) {
    suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerRepository.searchPartners(searchText)
    }
}
