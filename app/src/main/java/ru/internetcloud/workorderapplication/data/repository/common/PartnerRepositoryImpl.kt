package ru.internetcloud.workorderapplication.data.repository.common

import ru.internetcloud.workorderapplication.data.datasource.local.PartnerLocalDataSource
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

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
