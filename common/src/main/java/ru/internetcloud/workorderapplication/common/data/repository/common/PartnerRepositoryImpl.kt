package ru.internetcloud.workorderapplication.common.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.datasource.local.PartnerLocalDataSource
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.common.domain.repository.PartnerRepository

class PartnerRepositoryImpl @Inject constructor(
    private val partnerLocalDataSource: PartnerLocalDataSource
) : PartnerRepository {

    override suspend fun getPartnerList(): List<Partner> {
        return partnerLocalDataSource.getPartnerList()
    }

    override suspend fun addPartnerList(partnerList: List<Partner>) {
        partnerLocalDataSource.addPartnerList(partnerList)
    }

    override suspend fun deleteAllPartners() {
        partnerLocalDataSource.deleteAllPartners()
    }

    override suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerLocalDataSource.searchPartners(searchText)
    }
}
