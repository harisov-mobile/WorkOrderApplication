package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class DeleteDepartmentsUseCase(private val departmentRepository: DepartmentRepository) {
    suspend fun deleteAllDepartments() {
        return departmentRepository.deleteAllDepartments()
    }
}
