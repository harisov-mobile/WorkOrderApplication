package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class AddDepartmentUseCase(private val departmentRepository: DepartmentRepository) {
    suspend fun addDepartment(department: Department) {
        return departmentRepository.addDepartment(department)
    }
}
