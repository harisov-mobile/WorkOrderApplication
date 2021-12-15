package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.data.network.api.ApiClient
import ru.internetcloud.workorderapplication.data.network.dto.DepartmentResponse
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository

class RemoteDepartmentRepositoryImpl private constructor(application: Application) : DepartmentRepository {

    companion object {
        private var instance: RemoteDepartmentRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = RemoteDepartmentRepositoryImpl(application)
            }
        }

        fun get(): RemoteDepartmentRepositoryImpl {
            return instance ?: throw RuntimeException("RemoteDepartmentRepositoryImpl must be initialized.")
        }
    }

    private val departmentMapper = DepartmentMapper()

    override suspend fun getDepartmentList(): List<Department> {
        var departmentResponse = DepartmentResponse(emptyList())

        try {
            departmentResponse = ApiClient.getInstance().client.getDepartments()
        } catch (e: Exception) {
            // Log.i("rustam", e.toString())
            throw e
        }
        return departmentMapper.fromListDtoToListEntity(departmentResponse.departments)
    }

    override suspend fun addDepartment(department: Department) {
        throw RuntimeException("Error - method addDepartment is restricted in RemoteDepartmentRepositoryImpl")
    }

    override suspend fun getDepartment(id: String): Department? {
        throw RuntimeException("Error - method getDepartment is restricted in RemoteDepartmentRepositoryImpl")
        return null
    }

    override suspend fun deleteAllDepartments() {
        throw RuntimeException("Error - method deleteAllDepartments is restricted in RemoteDepartmentRepositoryImpl")
    }
}
