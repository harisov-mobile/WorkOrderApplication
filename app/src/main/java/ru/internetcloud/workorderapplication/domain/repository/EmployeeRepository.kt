package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.model.catalog.Employee

interface EmployeeRepository {

    suspend fun getEmployeeList(): List<Employee>

    suspend fun addEmployeeList(employeeList: List<Employee>)

    suspend fun addEmployee(employee: Employee)

    suspend fun deleteAllEmployees()

    suspend fun searchEmployees(searchText: String): List<Employee>
}
