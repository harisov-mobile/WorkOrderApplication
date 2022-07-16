package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import javax.inject.Inject
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class GetEmployeeListUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun getEmployeeList(): List<Employee> {
        return employeeRepository.getEmployeeList()
    }
}
