package ru.internetcloud.workorderapplication.data.repository.db

import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.repository.EmployeeRepository
import javax.inject.Inject

class DbEmployeeRepositoryImpl @Inject constructor(
    // application: Application
    private val appDao: AppDao,
    private val employeeMapper: EmployeeMapper
) : EmployeeRepository {

    companion object {
//        private var instance: DbEmployeeRepositoryImpl? = null
//
//        fun initialize(application: Application) {
//            if (instance == null) {
//                instance = DbEmployeeRepositoryImpl(application)
//            }
//        }
//
//        fun get(): DbEmployeeRepositoryImpl {
//            return instance ?: throw RuntimeException("DbEmployeeRepositoryImpl must be initialized.")
//        }
    }

    override suspend fun getEmployeeList(): List<Employee> {
        return employeeMapper.fromListDbModelToListEntity(appDao.getEmployeeList())
    }

    override suspend fun addEmployee(employee: Employee) {
        appDao.addEmployee(employeeMapper.fromEntityToDbModel(employee))
    }

    override suspend fun addEmployeeList(employeeList: List<Employee>) {
        appDao.addEmployeeList(employeeMapper.fromListEntityToListDbModel(employeeList))
    }

    override suspend fun getEmployee(id: String): Employee? {
        var employee: Employee? = null

        val employeeDbModel = appDao.getEmployee(id)

        employeeDbModel?.let {
            employee = employeeMapper.fromDbModelToEntity(it)
        }

        return employee
    }

    override suspend fun deleteAllEmployees() {
        appDao.deleteAllEmployees()
    }

    override suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeMapper.fromListDbModelToListEntity(appDao.searchEmployees("%$searchText%"))
    }
}
