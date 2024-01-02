package ru.internetcloud.workorderapplication.common.domain.repository

import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department

interface DepartmentRepository {

    suspend fun getDepartmentList(): List<Department>

    suspend fun addDepartment(department: Department)

    suspend fun deleteAllDepartments()

    suspend fun searchDepartments(searchText: String): List<Department>
}
