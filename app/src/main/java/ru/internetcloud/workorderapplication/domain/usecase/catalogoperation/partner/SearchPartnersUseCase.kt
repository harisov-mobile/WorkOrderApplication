package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class SearchPartnersUseCase(private val partnerRepository: PartnerRepository) {
    suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerRepository.searchPartners(searchText)
    }
}
