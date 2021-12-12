package ru.internetcloud.workorderapplication.domain.repository

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

interface RepairTypeRepository {

    fun getRepairTypeList(): LiveData<List<RepairType>>

    fun getRepairType(id: Int): RepairType?

    fun getRepairTypeById1C(id1C: String): RepairType?
}