package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.DepartmentDbModel
import ru.internetcloud.workorderapplication.common.data.network.dto.DepartmentDTO
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department

class DepartmentMapper @Inject constructor() {

    fun fromDtoToEntity(departmentDTO: DepartmentDTO): Department {
        return Department(
            id = departmentDTO.id,
            code1C = departmentDTO.code1C,
            name = departmentDTO.name
        )
    }

    fun fromListDtoToListEntity(list: List<DepartmentDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromDbModelToEntityWithNull(departmentDbModel: DepartmentDbModel?): Department? {
        var result: Department? = null
        if (departmentDbModel != null) {
            result = Department(
                id = departmentDbModel.id,
                code1C = departmentDbModel.code1C,
                name = departmentDbModel.name
            )
        }
        return result
    }

    fun fromDbModelToEntity(departmentDbModel: DepartmentDbModel): Department {
        return Department(
            id = departmentDbModel.id,
            code1C = departmentDbModel.code1C,
            name = departmentDbModel.name
        )
    }

    fun fromListDbModelToListEntity(list: List<DepartmentDbModel>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromEntityToDbModel(department: Department): DepartmentDbModel {
        return DepartmentDbModel(
            id = department.id,
            code1C = department.code1C,
            name = department.name
        )
    }
}
