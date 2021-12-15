package ru.internetcloud.workorderapplication.domain.repository

import ru.internetcloud.workorderapplication.domain.catalog.Department

interface DepartmentRepository {

    suspend fun getDepartmentList(): List<Department>

    suspend fun addDepartment(department: Department)

    suspend fun getDepartment(id: String): Department?

    suspend fun deleteAllDepartments()
}
