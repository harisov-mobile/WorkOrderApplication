package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class RemotePartnerRepositoryImpl @Inject constructor(
    private val partnerMapper: PartnerMapper
) : PartnerRepository {

    override suspend fun getPartnerList(): List<Partner> {
        val partnerResponse = ApiClient.getInstance().client.getPartners()
        return partnerMapper.fromListDtoToListEntity(partnerResponse.partners)
    }

    override suspend fun addPartnerList(partnerList: List<Partner>) {
        throw RuntimeException("Error - method addPartnerList is restricted in RemotePartnerRepositoryImpl")
    }

    override suspend fun getPartner(id: String): Partner? {
        throw RuntimeException("Error - method getPartner is restricted in RemotePartnerRepositoryImpl")
    }

    override suspend fun deleteAllPartners() {
        throw RuntimeException("Error - method deleteAllPartners is restricted in RemotePartnerRepositoryImpl")
    }

    override suspend fun searchPartners(searchText: String): List<Partner> {
        throw RuntimeException("Error - method searchPartners is restricted in RemotePartnerRepositoryImpl")
    }
}
