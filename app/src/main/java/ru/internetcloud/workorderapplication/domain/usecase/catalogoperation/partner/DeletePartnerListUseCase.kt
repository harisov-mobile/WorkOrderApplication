package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.partner

import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class DeletePartnerListUseCase(private val partnerRepository: PartnerRepository) {
    suspend fun deleteAllPartners() {
        return partnerRepository.deleteAllPartners()
    }
}
