package ru.internetcloud.workorderapplication.data.repository.remote

import android.util.Log
import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.DepartmentResponse
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class RemoteDepartmentRepositoryImpl @Inject constructor(
    private val departmentMapper: DepartmentMapper
) : DepartmentRepository {

    companion object {
//        private var instance: RemoteDepartmentRepositoryImpl? = null
//
//        fun initialize() {
//            if (instance == null) {
//                instance = RemoteDepartmentRepositoryImpl()
//            }
//        }
//
//        fun get(): RemoteDepartmentRepositoryImpl {
//            return instance ?: throw RuntimeException("RemoteDepartmentRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getDepartmentList(): List<Department> {
        var departmentResponse = DepartmentResponse(emptyList())

        try {
            departmentResponse = ApiClient.getInstance().client.getDepartments()
        } catch (e: Exception) {
            // ничего не делаю
            Log.i("rustam", "Ошибка при getDepartments " + e.toString())
        }

        Log.i("rustam", "прошел дальше")

        return departmentMapper.fromListDtoToListEntity(departmentResponse.departments)
    }

    override suspend fun addDepartment(department: Department) {
        throw RuntimeException("Error - method addDepartment is restricted in RemoteDepartmentRepositoryImpl")
    }

    override suspend fun getDepartment(id: String): Department? {
        throw RuntimeException("Error - method getDepartment is restricted in RemoteDepartmentRepositoryImpl")
    }

    override suspend fun deleteAllDepartments() {
        throw RuntimeException("Error - method deleteAllDepartments is restricted in RemoteDepartmentRepositoryImpl")
    }

    override suspend fun searchDepartments(searchText: String): List<Department> {
        throw RuntimeException("Error - method searchDepartments is restricted in RemoteDepartmentRepositoryImpl")
    }
}
