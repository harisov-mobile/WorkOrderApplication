package ru.internetcloud.workorderapplication.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.domain.catalog.Department

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
    suspend fun getDepartment(id: String): Department? {
        var department: Department? = null

        val departmentDbModel = appDao.getDepartment(id)

        departmentDbModel?.let {
            department = departmentMapper.fromDbModelToEntity(it)
        }

        return department
    }

    suspend fun deleteAllDepartments() {
        appDao.deleteAllDepartments()
    }
    suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentMapper.fromListDbModelToListEntity(appDao.searhDepartments("%$searchText%"))
    }
}
