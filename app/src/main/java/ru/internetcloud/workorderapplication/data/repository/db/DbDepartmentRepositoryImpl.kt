package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.DepartmentMapper
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class DbDepartmentRepositoryImpl @Inject constructor(
    // application: Application
    private val appDao: AppDao,
    private val departmentMapper: DepartmentMapper
) : DepartmentRepository {

    companion object {
//        private var instance: DbDepartmentRepositoryImpl? = null
//
//        fun initialize(application: Application) {
//            if (instance == null) {
//                instance = DbDepartmentRepositoryImpl(application)
//            }
//        }
//
//        fun get(): DbDepartmentRepositoryImpl {
//            return instance ?: throw RuntimeException("DbDepartmentRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getDepartmentList(): List<Department> {
        return departmentMapper.fromListDbModelToListEntity(appDao.getDepartmentList())
    }

    override suspend fun addDepartment(department: Department) {
        appDao.addDepartment(departmentMapper.fromEntityToDbModel(department))
    }

    override suspend fun getDepartment(id: String): Department? {
        var department: Department? = null

        val departmentDbModel = appDao.getDepartment(id)

        departmentDbModel?.let {
            department = departmentMapper.fromDbModelToEntity(it)
        }

        return department
    }

    override suspend fun deleteAllDepartments() {
        appDao.deleteAllDepartments()
    }

    override suspend fun searchDepartments(searchText: String): List<Department> {
        return departmentMapper.fromListDbModelToListEntity(appDao.searhDepartments("%$searchText%"))
    }
}
