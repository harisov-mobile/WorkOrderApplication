package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.model.DefaultRepairTypeJobDetailDbModel
import ru.internetcloud.workorderapplication.data.model.DefaultRepairTypeJobDetailWithRequisities
import ru.internetcloud.workorderapplication.data.network.dto.DefaultRepairTypeJobDetailDTO
import ru.internetcloud.workorderapplication.domain.catalog.DefaultRepairTypeJobDetail
import javax.inject.Inject

class DefaultRepairTypeJobDetailMapper @Inject constructor(
    private val carModelMapper: CarModelMapper,
    private val carJobMapper: CarJobMapper
) {

    fun fromDtoToDbModel(defaultJobDetailDTO: DefaultRepairTypeJobDetailDTO): DefaultRepairTypeJobDetailDbModel {
        return DefaultRepairTypeJobDetailDbModel(
            id = defaultJobDetailDTO.id,
            lineNumber = defaultJobDetailDTO.lineNumber,
            carModelId = defaultJobDetailDTO.carModelId,
            carJobId = defaultJobDetailDTO.carJobId,
            quantity = defaultJobDetailDTO.quantity,
            repairTypeId = defaultJobDetailDTO.repairTypeId
        )
    }

    fun fromListDbModelToListEntity(list: List<DefaultRepairTypeJobDetailWithRequisities>): MutableList<DefaultRepairTypeJobDetail> {
        val sortedList = list.sortedBy {
            it.defaultRepairTypeJobDetailDbModel.lineNumber
        }

        val result: MutableList<DefaultRepairTypeJobDetail> = mutableListOf()

        sortedList.forEach {
            result.add(fromDbToEntity(it))
        }

        return result
    }

    fun fromDbToEntity(defaultRepairTypeJobDetailWithRequisities: DefaultRepairTypeJobDetailWithRequisities): DefaultRepairTypeJobDetail {
        return DefaultRepairTypeJobDetail(
            id = defaultRepairTypeJobDetailWithRequisities.defaultRepairTypeJobDetailDbModel.id,
            lineNumber = defaultRepairTypeJobDetailWithRequisities.defaultRepairTypeJobDetailDbModel.lineNumber,
            carModel = carModelMapper.fromDbModelToEntityWithNull(defaultRepairTypeJobDetailWithRequisities.carModel),
            carJob = carJobMapper.fromDbModelToEntityWithNull(defaultRepairTypeJobDetailWithRequisities.carJob),
            quantity = defaultRepairTypeJobDetailWithRequisities.defaultRepairTypeJobDetailDbModel.quantity
        )
    }

    fun fromListDtoToDbModel(list: List<DefaultRepairTypeJobDetailDTO>): List<DefaultRepairTypeJobDetailDbModel> {
        return list.map { fromDtoToDbModel(it) }
    }
}
