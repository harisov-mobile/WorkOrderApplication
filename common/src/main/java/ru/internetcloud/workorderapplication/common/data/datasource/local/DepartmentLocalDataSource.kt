package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department

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
