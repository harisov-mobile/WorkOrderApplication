package ru.internetcloud.workorderapplication.data.repository.common

import ru.internetcloud.workorderapplication.data.datasource.local.RepairTypeLocalDataSource
import ru.internetcloud.workorderapplication.domain.model.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class RepairTypeRepositoryImpl @Inject constructor(
    private val repairTypeLocalDataSource: RepairTypeLocalDataSource
) : RepairTypeRepository {

    override suspend fun getRepairTypeList(): List<RepairType> {
        return repairTypeLocalDataSource.getRepairTypeList()
    }

    override suspend fun addRepairType(repairType: RepairType) {
        repairTypeLocalDataSource.addRepairType(repairType)
    }

    override suspend fun addRepairTypeList(repairTypeList: List<RepairType>) {
        addRepairTypeList(repairTypeList)
    }

    override suspend fun deleteAllRepairTypes() {
        repairTypeLocalDataSource.deleteAllRepairTypes()
    }

    override suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        return repairTypeLocalDataSource.searchRepairTypes(searchText)
    }

    override suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail> {
        return repairTypeLocalDataSource.getDefaultRepairTypeJobDetails(repairType)
    }
}
