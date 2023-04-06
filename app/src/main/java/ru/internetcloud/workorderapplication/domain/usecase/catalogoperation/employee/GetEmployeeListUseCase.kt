package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class GetEmployeeListUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun getEmployeeList(): List<Employee> {
        return employeeRepository.getEmployeeList()
    }
}
