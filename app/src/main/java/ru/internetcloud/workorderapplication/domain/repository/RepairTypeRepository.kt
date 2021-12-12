package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.RepairType

interface RepairTypeRepository {

    suspend fun getRepairTypeList(): List<RepairType>

    fun getRepairType(id: Int): RepairType?

    fun getRepairTypeById1C(id1C: String): RepairType?
}
