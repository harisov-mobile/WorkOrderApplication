package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository

class SearchRepairTypesUseCase @Inject constructor(
    private val repairTypeRepository: RepairTypeRepository
) {
    suspend fun searchRepairTypes(searchText: String): List<RepairType> {
        return repairTypeRepository.searchRepairTypes(searchText)
    }
}
