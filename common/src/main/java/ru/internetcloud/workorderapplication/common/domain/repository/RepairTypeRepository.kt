package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.catalog.DefaultRepairTypeJobDetail
import ru.internetcloud.workorderapplication.common.domain.model.catalog.RepairType

interface RepairTypeRepository {

    suspend fun getRepairTypeList(): List<RepairType>

    suspend fun addRepairType(repairType: RepairType)

    suspend fun addRepairTypeList(repairTypeList: List<RepairType>)

    suspend fun deleteAllRepairTypes()

    suspend fun searchRepairTypes(searchText: String): List<RepairType>

    suspend fun getDefaultRepairTypeJobDetails(repairType: RepairType): List<DefaultRepairTypeJobDetail>
}
