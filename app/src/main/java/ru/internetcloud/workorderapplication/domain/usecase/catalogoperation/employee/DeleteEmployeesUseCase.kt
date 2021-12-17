package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class DeleteEmployeesUseCase(private val employeeRepository: EmployeeRepository) {
    suspend fun deleteAllEmployees() {
        return employeeRepository.deleteAllEmployees()
    }
}
