package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbDepartmentRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class DeleteDepartmentsUseCase @Inject constructor(@DbDepartmentRepositoryQualifier private val departmentRepository: DepartmentRepository) {
    suspend fun deleteAllDepartments() {
        return departmentRepository.deleteAllDepartments()
    }
}
