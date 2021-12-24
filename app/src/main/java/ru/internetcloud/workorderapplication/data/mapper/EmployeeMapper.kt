package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.EmployeeDbModel
import ru.internetcloud.workorderapplication.data.network.dto.EmployeeDTO
import ru.internetcloud.workorderapplication.domain.catalog.Employee

class EmployeeMapper {

    fun fromDtoToEntity(employeeDTO: EmployeeDTO): Employee {
        return Employee(
            id = employeeDTO.id,
            code1C = employeeDTO.code1C,
            name = employeeDTO.name
        )
    }

    fun fromListDtoToListEntity(list: List<EmployeeDTO>) = list.map {
        fromDtoToEntity(it)
    }

    fun fromDbModelToEntity(employeeDbModel: EmployeeDbModel?): Employee? {
        var result: Employee? = null
        if (employeeDbModel != null) {
            result = Employee(
                id = employeeDbModel.id,
                code1C = employeeDbModel.code1C,
                name = employeeDbModel.name
            )
        }
        return result
    }

    fun fromDbModelToEntityWithoutNull(employeeDbModel: EmployeeDbModel): Employee {
            return Employee(
                id = employeeDbModel.id,
                code1C = employeeDbModel.code1C,
                name = employeeDbModel.name
            )
    }

    fun fromListDbModelToListEntity(list: List<EmployeeDbModel>) = list.map {
        fromDbModelToEntityWithoutNull(it)
    }

    fun fromListEntityToListDbModel(list: List<Employee>) = list.map {
        fromEntityToDbModel(it)
    }

    fun fromEntityToDbModel(employee: Employee): EmployeeDbModel {
        return EmployeeDbModel(
            id = employee.id,
            code1C = employee.code1C,
            name = employee.name
        )
    }
}
