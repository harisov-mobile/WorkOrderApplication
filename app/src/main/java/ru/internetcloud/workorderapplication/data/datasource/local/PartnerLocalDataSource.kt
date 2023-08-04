package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import javax.inject.Inject

class PartnerLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val partnerMapper: PartnerMapper
) {

    suspend fun getPartnerList(): List<Partner> {
        return partnerMapper.fromListDbModelToListEntity(appDao.getPartnerList())
    }

    suspend fun addPartnerList(partnerList: List<Partner>) {
        appDao.addPartnerList(partnerMapper.fromListEntityToListDbModel(partnerList))
    }

    suspend fun deleteAllPartners() {
        appDao.deleteAllPartners()
    }

    suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerMapper.fromListDbModelToListEntity(appDao.searhPartners("%$searchText%"))
    }
}
