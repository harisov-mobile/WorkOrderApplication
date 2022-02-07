package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class GetDepartmentListUseCase(private val departmentRepository: DepartmentRepository) {
    suspend fun getDepartmentList(): List<Department> {
        return departmentRepository.getDepartmentList()
    }
}
