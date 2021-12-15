package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.Employee

interface EmployeeRepository {

    suspend fun getEmployeeList(): List<Employee>

    suspend fun addEmployee(carJob: Employee)

    suspend fun getEmployee(id: String): Employee?

    suspend fun deleteAllEmployees()
}