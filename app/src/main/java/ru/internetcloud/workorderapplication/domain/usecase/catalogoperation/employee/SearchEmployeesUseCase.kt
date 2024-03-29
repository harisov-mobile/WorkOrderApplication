package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class SearchEmployeesUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeRepository.searchEmployees(searchText)
    }
}
