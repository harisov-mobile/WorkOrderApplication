package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.employee

import ru.internetcloud.workorderapplication.di.qualifiers.repository.DbEmployeeRepositoryQualifier
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class AddEmployeeListUseCase @Inject constructor(
    @DbEmployeeRepositoryQualifier private val employeeRepository: EmployeeRepository
) {

    suspend fun addEmployeeList(employeeList: List<Employee>) {
        return employeeRepository.addEmployeeList(employeeList)
    }
}
