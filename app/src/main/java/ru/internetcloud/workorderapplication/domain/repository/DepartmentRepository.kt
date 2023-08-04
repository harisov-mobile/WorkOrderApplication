package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.model.catalog.Department

interface DepartmentRepository {

    suspend fun getDepartmentList(): List<Department>

    suspend fun addDepartment(department: Department)

    suspend fun deleteAllDepartments()

    suspend fun searchDepartments(searchText: String): List<Department>
}
