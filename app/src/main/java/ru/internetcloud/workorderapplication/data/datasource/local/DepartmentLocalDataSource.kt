package ru.internetcloud.workorderapplication.data.datasource.local

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.domain.catalog.Department
import javax.inject.Inject

class DepartmentLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val departmentMapper: DepartmentMapper
) {

    suspend fun getDepartmentList(): List<Department> {
        return departmentMapper.fromListDbModelToListEntity(appDao.getDepartmentList())
    }

    suspend fun addDepartment(department: Department) {
        appDao.addDepartment(departmentMapper.fromEntityToDbModel(department))
    }

    suspend fun deleteAllDepartments() {
        appDao.deleteAllDepartments()
    }

    suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentMapper.fromListDbModelToListEntity(appDao.searhDepartments("%$searchText%"))
    }
}
