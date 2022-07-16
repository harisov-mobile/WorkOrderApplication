package ru.internetcloud.workorderapplication.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.domain.catalog.Partner

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

    suspend fun getPartner(id: String): Partner? {
        var partner: Partner? = null

        val partnerDbModel = appDao.getPartner(id)

        partnerDbModel?.let {
            partner = partnerMapper.fromDbModelToEntity(it)
        }

        return partner
    }

    suspend fun deleteAllPartners() {
        appDao.deleteAllPartners()
    }

    suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerMapper.fromListDbModelToListEntity(appDao.searhPartners("%$searchText%"))
    }
}
