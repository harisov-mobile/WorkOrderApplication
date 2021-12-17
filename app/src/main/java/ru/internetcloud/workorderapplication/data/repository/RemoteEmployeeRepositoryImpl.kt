package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.EmployeeResponse
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository

class RemoteEmployeeRepositoryImpl private constructor(application: Application) : EmployeeRepository {

    companion object {
        private var instance: RemoteEmployeeRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = RemoteEmployeeRepositoryImpl(application)
            }
        }

        fun get(): RemoteEmployeeRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteEmployeeRepositoryImpl must be initialized.")
        }
    }

    private val employeeMapper = EmployeeMapper()

    override suspend fun getEmployeeList(): List<Employee> {
        var employeeResponse = EmployeeResponse(emptyList())

        try {
            employeeResponse = ApiClient.getInstance().client.getEmployees()
        } catch (e: Exception) {
            // ничего не делаю
        }
        return employeeMapper.fromListDtoToListEntity(employeeResponse.employees)
    }

    override suspend fun addEmployeeList(employeeList: List<Employee>) {
        throw RuntimeException("Error - method addEmployeeList is restricted in RemoteEmployeeRepositoryImpl")
    }

    override suspend fun addEmployee(employee: Employee) {
        throw RuntimeException("Error - method addEmployee is restricted in RemoteEmployeeRepositoryImpl")
    }

    override suspend fun getEmployee(id: String): Employee? {
        throw RuntimeException("Error - method getEmployee is restricted in RemoteEmployeeRepositoryImpl")
        return null
    }

    override suspend fun deleteAllEmployees() {
        throw RuntimeException("Error - method deleteAllEmployees is restricted in RemoteEmployeeRepositoryImpl")
    }
}
