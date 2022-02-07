package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class AddEmployeeUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun addEmployee(employee: Employee) {
        return employeeRepository.addEmployee(employee)
    }
}
