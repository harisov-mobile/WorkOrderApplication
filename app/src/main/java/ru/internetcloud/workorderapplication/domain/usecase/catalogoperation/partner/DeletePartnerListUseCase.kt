package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbPartnerRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class DeletePartnerListUseCase @Inject constructor(
    @DbPartnerRepositoryQualifier private val partnerRepository: PartnerRepository
) {

    suspend fun deleteAllPartners() {
        return partnerRepository.deleteAllPartners()
    }
}
