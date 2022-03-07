package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository
import javax.inject.Inject

class DbPartnerRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val partnerMapper: PartnerMapper
) : PartnerRepository {

    override suspend fun getPartnerList(): List<Partner> {
        return partnerMapper.fromListDbModelToListEntity(appDao.getPartnerList())
    }

    override suspend fun addPartnerList(partnerList: List<Partner>) {
        appDao.addPartnerList(partnerMapper.fromListEntityToListDbModel(partnerList))
    }

    override suspend fun getPartner(id: String): Partner? {
        var partner: Partner? = null

        val partnerDbModel = appDao.getPartner(id)

        partnerDbModel?.let {
            partner = partnerMapper.fromDbModelToEntity(it)
        }

        return partner
    }

    override suspend fun deleteAllPartners() {
        appDao.deleteAllPartners()
    }

    override suspend fun searchPartners(searchText: String): List<Partner> {
        return partnerMapper.fromListDbModelToListEntity(appDao.searhPartners("%$searchText%"))
    }
}
