package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbPartnerRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class SearchPartnersUseCase @Inject constructor(
    @DbPartnerRepositoryQualifier
    private val partnerRepository: PartnerRepository
) {

    suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerRepository.searchPartners(searchText)
    }
}
