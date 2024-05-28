package ru.internetcloud.workorderapplication.common.data.datasource.local

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.database.AppDao
import ru.internetcloud.workorderapplication.common.data.mapper.EmployeeMapper
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee

class EmployeeLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val employeeMapper: EmployeeMapper
) {

    suspend fun getEmployeeList(): List<Employee> {
        return employeeMapper.fromListDbModelToListEntity(appDao.getEmployeeList())
    }
    suspend fun addEmployee(employee: Employee) {
        appDao.addEmployee(employeeMapper.fromEntityToDbModel(employee))
    }
    suspend fun addEmployeeList(employeeList: List<Employee>) {
        appDao.addEmployeeList(employeeMapper.fromListEntityToListDbModel(employeeList))
    }

    suspend fun deleteAllEmployees() {
        appDao.deleteAllEmployees()
    }
    suspend fun searchEmployees(searchText: String): List<Employee> {
        return employeeMapper.fromListDbModelToListEntity(appDao.searchEmployees("%$searchText%"))
    }
}
