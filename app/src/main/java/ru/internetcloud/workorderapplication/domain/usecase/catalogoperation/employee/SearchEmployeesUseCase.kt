package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class SearchEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeRepository.searchEmployees(searchText)
    }
}
