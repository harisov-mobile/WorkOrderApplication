package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class GetEmployeeUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun getEmployee(id: String): Employee? {
        return employeeRepository.getEmployee(id)
    }
}
