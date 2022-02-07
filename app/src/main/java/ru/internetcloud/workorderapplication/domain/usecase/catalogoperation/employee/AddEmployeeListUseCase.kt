package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class AddEmployeeListUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun addEmployeeList(employeeList: List<Employee>) {
        return employeeRepository.addEmployeeList(employeeList)
    }
}
