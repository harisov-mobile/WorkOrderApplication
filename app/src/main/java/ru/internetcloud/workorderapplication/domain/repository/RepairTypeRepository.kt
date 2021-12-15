package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.RepairType

interface RepairTypeRepository {

    suspend fun getRepairTypeList(): List<RepairType>

    suspend fun addRepairType(repairType: RepairType)

    suspend fun getRepairType(id1C: String): RepairType?
}
