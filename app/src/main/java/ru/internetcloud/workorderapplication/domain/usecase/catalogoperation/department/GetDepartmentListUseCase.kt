package ru.internetcloud.workorderapplication.domain.usecase.catalogoperation.department

import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.repository.DepartmentRepository
import javax.inject.Inject

class GetDepartmentListUseCase @Inject constructor(
    private val departmentRepository: DepartmentRepository
) {
    suspend fun getDepartmentList(): List<Department> {
        return departmentRepository.getDepartmentList()
    }
}
