package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class GetEmployeeUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun getEmployee(id: String): Employee? {
        return employeeRepository.getEmployee(id)
    }
}
