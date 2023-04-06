package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DefaultRepairTypeJobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.RepairTypeMapper
import ru.internetcloud.workorderapplication.data.model.DefaultRepairTypeJobDetailDbModel
import ru.internetcloud.workorderapplication.domain.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import javax.inject.Inject

class RepairTypeLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val repairTypeMapper: RepairTypeMapper,
    private val defaultRepairTypeJobDetailMapper: DefaultRepairTypeJobDetailMapper
) {
    suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.getRepairTypeList())
    }

    suspend fun addRepairType(repairType: RepairType) {
        appDao.addRepairType(repairTypeMapper.fromEntityToDbModel(repairType))
    }

    suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
        appDao.addRepairTypeList(repairTypeMapper.fromListEntityToListDbModel(repairTypeList))
    }

    suspend fun deleteAllRepairTypes() {
        appDao.deleteAllRepairTypes()
    }

    suspend fun deleteAllDefaultRepairTypeJobDetails() {
        appDao.deleteAllDefaultRepairTypeJobDetails()
    }

    suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        return repairTypeMapper.fromListDbModelToListEntity(appDao.searhRepairTypes("%$searchText%"))
    }

    suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail> {
        return defaultRepairTypeJobDetailMapper.fromListDbModelToListEntity(appDao.getDefaultRepairTypeJobDetails(repairType.id))
    }

    suspend fun addDefaultRepairTypeJobDetailList(list: List<DefaultRepairTypeJobDetailDbModel>) {
        appDao.addDefaultRepairTypeJobDetailList(list)
    }
}
