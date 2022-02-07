package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.RepairType

interface RepairTypeRepository {

    suspend fun getRepairTypeList(): List<RepairType>

    suspend fun addRepairType(repairType: RepairType)

    suspend fun addRepairTypeList(repairTypeList: List<RepairType>)

    suspend fun getRepairType(id: String): RepairType?

    suspend fun deleteAllRepairTypes()

    suspend fun searchRepairTypes(searchText: String): List<RepairType>
}
