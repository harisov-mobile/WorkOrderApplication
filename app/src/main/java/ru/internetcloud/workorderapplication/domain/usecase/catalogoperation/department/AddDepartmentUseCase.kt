package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class AddDepartmentUseCase @Inject constructor(private val departmentRepository: DepartmentRepository) {

    suspend fun addDepartment(department: Department) {
        return departmentRepository.addDepartment(department)
    }
}
