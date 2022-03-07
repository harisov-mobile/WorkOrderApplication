package ru.internetcloud.workorderapplication.data.repository.remote

import ru.internetcloud.workorderapplication.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.EmployeeResponse
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class RemoteEmployeeRepositoryImpl @Inject constructor(
    private val employeeMapper: EmployeeMapper
) : EmployeeRepository {

    companion object {
//        private var instance: RemoteEmployeeRepositoryImpl? = null
//
//        fun initialize() {
//            if (instance == null) {
//                instance = RemoteEmployeeRepositoryImpl()
//            }
//        }
//
//        fun get(): RemoteEmployeeRepositoryImpl {
//            return instance ?: throw RuntimeException("RemoteEmployeeRepositoryImpl must be initialized.")
//        }
    }

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
    }

    override suspend fun deleteAllEmployees() {
        throw RuntimeException("Error - method deleteAllEmployees is restricted in RemoteEmployeeRepositoryImpl")
    }

    override suspend fun searchEmployees(searchText: String): List<Employee> {
        throw RuntimeException("Error - method searchEmployees is restricted in RemoteEmployeeRepositoryImpl")
    }
}
