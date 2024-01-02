package ru.internetcloud.workorderapplication.common.domain.usecase.catalogoperation.department

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.common.domain.repository.DepartmentRepository

class GetDepartmentListUseCase @Inject constructor(
    private val departmentRepository: DepartmentRepository
) {
    suspend fun getDepartmentList(): List<Department> {
        return departmentRepository.getDepartmentList()
    }
}
