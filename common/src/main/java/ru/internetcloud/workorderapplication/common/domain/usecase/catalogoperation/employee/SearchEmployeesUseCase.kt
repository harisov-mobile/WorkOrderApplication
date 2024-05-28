package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.employee

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.common.domain.repository.EmployeeRepository

class SearchEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeRepository.searchEmployees(searchText)
    }
}
