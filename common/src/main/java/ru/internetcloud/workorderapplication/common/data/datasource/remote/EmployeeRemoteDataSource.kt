package ru.internetcloud.workorderapplication.common.data.datasource.remote

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.common.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee

class EmployeeRemoteDataSource @Inject constructor(
    private val employeeMapper: EmployeeMapper
) {

    suspend fun getEmployeeList(): List<Employee> {
        val employeeResponse = ApiClient.getInstance().client.getEmployees()
        return employeeMapper.fromListDtoToListEntity(employeeResponse.employees)
    }
}
