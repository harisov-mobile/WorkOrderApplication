package ru.internetcloud.workorderapplication.common.data.repository.common

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.datasource.local.EmployeeLocalDataSource
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.common.domain.repository.EmployeeRepository

class EmployeeRepositoryImpl @Inject constructor(
    private val employeeLocalDataSource: EmployeeLocalDataSource
) : EmployeeRepository {

    override suspend fun getEmployeeList(): List<Employee> {
        return employeeLocalDataSource.getEmployeeList()
    }

    override suspend fun addEmployeeList(employeeList: List<Employee>) {
        employeeLocalDataSource.addEmployeeList(employeeList)
    }

    override suspend fun addEmployee(employee: Employee) {
        employeeLocalDataSource.addEmployee(employee)
    }

    override suspend fun deleteAllEmployees() {
        employeeLocalDataSource.deleteAllEmployees()
    }

    override suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeLocalDataSource.searchEmployees(searchText)
    }
}
