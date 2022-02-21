package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class DeletePartnerListUseCase @Inject constructor(private val partnerRepository: PartnerRepository) {

    suspend fun deleteAllPartners() {
        return partnerRepository.deleteAllPartners()
    }
}
