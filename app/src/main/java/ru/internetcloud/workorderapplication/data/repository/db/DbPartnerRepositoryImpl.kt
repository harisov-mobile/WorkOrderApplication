package ru.internetcloud.workorderapplication.data.repository.db

import android.app.Application
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.data.mapper.PartnerMapper
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.repository.PartnerRepository

class DbPartnerRepositoryImpl private constructor(application: Application) : PartnerRepository {

    private val appDao = AppDatabase.getInstance(application).appDao()
    private val partnerMapper = PartnerMapper()

    companion object {
        private var instance: DbPartnerRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DbPartnerRepositoryImpl(application)
            }
        }

        fun get(): DbPartnerRepositoryImpl {
            return instance ?: throw RuntimeException("DbPartnerRepositoryImpl must be initialized.")
        }
    }

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
