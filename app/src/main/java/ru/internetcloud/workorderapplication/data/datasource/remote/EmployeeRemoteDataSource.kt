package ru.internetcloud.workorderapplication.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Employee

class EmployeeRemoteDataSource @Inject constructor(
    private val employeeMapper: EmployeeMapper
) {

    suspend fun getEmployeeList(): List<Employee> {
        var employeeResponse = ApiClient.getInstance().client.getEmployees()
        return employeeMapper.fromListDtoToListEntity(employeeResponse.employees)
    }
}
