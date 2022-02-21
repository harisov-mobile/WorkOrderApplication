package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class DeleteEmployeesUseCase @Inject constructor(private val employeeRepository: EmployeeRepository) {
    suspend fun deleteAllEmployees() {
        return employeeRepository.deleteAllEmployees()
    }
}
