package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation

import androidx.lifecycle.LiveData
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class GetRepairTypeListUseCase(private val repairTypeRepository: RepairTypeRepository) {
    fun getRepairTypeList(): LiveData<List<RepairType>> {
        return repairTypeRepository.getRepairTypeList()
    }
}