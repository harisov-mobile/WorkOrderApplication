package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner

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
