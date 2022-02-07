package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class GetEmployeeListUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun getEmployeeList(): List<Employee> {
        return employeeRepository.getEmployeeList()
    }
}
