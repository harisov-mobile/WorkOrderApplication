package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.repairtype

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbRepairTypeRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.RepairTypeRepository
import javax.inject.Inject

class DeleteRepairTypesUseCase @Inject constructor(@DbRepairTypeRepositoryQualifier private val repairTypeRepository: RepairTypeRepository) {

    suspend fun deleteAllRepairTypes() {
        return repairTypeRepository.deleteAllRepairTypes()
    }
}
