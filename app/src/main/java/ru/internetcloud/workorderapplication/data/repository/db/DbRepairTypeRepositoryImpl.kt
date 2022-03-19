package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class DbRepairTypeRepositoryImpl @Inject constructor(
    private val appDao: AppDao,
    private val repairTypeMapper: RepairTypeMapper
) : RepairTypeRepository {

    override suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.getRepairTypeList())
    }

    override suspend fun addRepairType(repairType: RepairType) {
        appDao.addRepairType(repairTypeMapper.fromEntityToDbModel(repairType))
    }

    override suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
            appDao.addRepairTypeList(repairTypeMapper.fromListEntityToListDbModel(repairTypeList))
    }

    override suspend fun getRepairType(id: String): RepairType? {

        var repairType: RepairType? = null

        val repairTypeDbModel = appDao.getRepairType(id)

        repairTypeDbModel?.let {
            repairType = repairTypeMapper.fromDbModelToEntity(it)
        }

        return repairType
    }

    override suspend fun deleteAllRepairTypes() {
        appDao.deleteAllRepairTypes()
    }

    override suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.searhRepairTypes("%$searchText%"))
    }
}
