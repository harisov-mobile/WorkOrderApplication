package ru.internetcloud.workorderapplication.data.datasource.remote

import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.domain.catalog.Department
import javax.inject.Inject

class DepartmentRemoteDataSource @Inject constructor(
    private val departmentMapper: DepartmentMapper
) {

    suspend fun getDepartmentList(): List<Department> {
        val departmentResponse = ApiClient.getInstance().client.getDepartments()
        return departmentMapper.fromListDtoToListEntity(departmentResponse.departments)
    }
}
