package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.PartnerResponse
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class RemotePartnerRepositoryImpl private constructor(application: Application) : PartnerRepository {

    companion object {
        private var instance: RemotePartnerRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = RemotePartnerRepositoryImpl(application)
            }
        }

        fun get(): RemotePartnerRepositoryImpl {
            return instance ?: throw RuntimeException("RemotePartnerRepositoryImpl must be initialized.")
        }
    }

    private val partnerMapper = PartnerMapper()

    override suspend fun getPartnerList(): List<Partner> {
        var partnerResponse = PartnerResponse(emptyList())

        try {
            partnerResponse = ApiClient.getInstance().client.getPartners()
        } catch (e: Exception) {
            // ничего не делаю
        }

        return partnerMapper.fromListDtoToListEntity(partnerResponse.partners)
    }

    override suspend fun addPartnerList(partnerList: List<Partner>) {
        throw RuntimeException("Error - method addPartnerList is restricted in RemotePartnerRepositoryImpl")
    }

    override suspend fun getPartner(id: String): Partner? {
        throw RuntimeException("Error - method getPartner is restricted in RemotePartnerRepositoryImpl")
        return null
    }

    override suspend fun deleteAllPartners() {
        throw RuntimeException("Error - method deleteAllPartners is restricted in RemotePartnerRepositoryImpl")
    }
}
