package ru.internetcloud.workorderapplication.data.repository.common

import ru.internetcloud.workorderapplication.data.datasource.local.DepartmentLocalDataSource
import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class DepartmentRepositoryImpl @Inject constructor(
    private val departmentLocalDataSource: DepartmentLocalDataSource
) : DepartmentRepository {

    override suspend fun getDepartmentList(): List<Department> {
        return departmentLocalDataSource.getDepartmentList()
    }

    override suspend fun addDepartment(department: Department) {
        departmentLocalDataSource.addDepartment(department)
    }

    override suspend fun deleteAllDepartments() {
        deleteAllDepartments()
    }

    override suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentLocalDataSource.searchDepartments(searchText)
    }
}
