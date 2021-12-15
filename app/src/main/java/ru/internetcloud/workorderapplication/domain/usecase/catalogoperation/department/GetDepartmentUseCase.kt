package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class GetDepartmentUseCase(private val departmentRepository: DepartmentRepository) {
    suspend fun getDepartment(id: String): Department? {
        return departmentRepository.getDepartment(id)
    }
}
