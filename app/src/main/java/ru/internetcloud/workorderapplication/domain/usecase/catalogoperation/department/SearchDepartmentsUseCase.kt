package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class SearchDepartmentsUseCase @Inject constructor(
    private val departmentRepository: DepartmentRepository
) {
    suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentRepository.searchDepartments(searchText)
    }
}
