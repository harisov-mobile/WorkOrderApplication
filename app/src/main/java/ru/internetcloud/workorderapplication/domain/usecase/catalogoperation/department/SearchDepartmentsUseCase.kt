package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class SearchDepartmentsUseCase(private val departmentRepository: DepartmentRepository) {
    suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentRepository.searchDepartments(searchText)
    }
}
