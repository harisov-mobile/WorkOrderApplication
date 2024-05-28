package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.employee

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.common.domain.repository.EmployeeRepository

class GetEmployeeListUseCase @Inject constructor(
    private val employeeRepository: EmployeeRepository
) {
    suspend fun getEmployeeList(): List<Employee> {
        return employeeRepository.getEmployeeList()
    }
}
