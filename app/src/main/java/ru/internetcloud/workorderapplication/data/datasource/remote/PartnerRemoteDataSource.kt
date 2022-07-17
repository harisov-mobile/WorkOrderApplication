package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Partner

class PartnerRemoteDataSource @Inject constructor(
    private val partnerMapper: PartnerMapper
) {
    suspend fun getPartnerList(): List<Partner> {
        val partnerResponse = ApiClient.getInstance().client.getPartners()
        return partnerMapper.fromListDtoToListEntity(partnerResponse.partners)
    }
}
