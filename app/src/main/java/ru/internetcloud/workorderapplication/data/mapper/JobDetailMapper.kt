package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.entity.JobDetailWithRequisities
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.dto.JobDetailDTO
import ru.internetcloud.workorderapplication.domain.document.JobDetail

class JobDetailMapper {

    private val carJobMapper = CarJobMapper()
    private val workingHourMapper = WorkingHourMapper()

    fun fromDtoToDbModel(jobDetailDTO: JobDetailDTO): JobDetailDbModel {
        return JobDetailDbModel(
            id = jobDetailDTO.id,
            carJobId = jobDetailDTO.carJobId,
            quantity = jobDetailDTO.quantity,
            timeNorm = jobDetailDTO.timeNorm,
            workingHourId = jobDetailDTO.workingHourId,
            sum = jobDetailDTO.sum,
            workOrderId = jobDetailDTO.workOrderId
        )
    }

    fun fromDbToEntityWithNull(jobDetailWithRequisities: JobDetailWithRequisities?): JobDetail? {
        var result: JobDetail? = null
        jobDetailWithRequisities?.let {
            result = JobDetail(
                id = jobDetailWithRequisities.jobDetailDbModel.id,
                carJob = carJobMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.carJob),
                quantity = jobDetailWithRequisities.jobDetailDbModel.quantity,
                timeNorm = jobDetailWithRequisities.jobDetailDbModel.timeNorm,
                workingHour = workingHourMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.workingHour),
                sum = jobDetailWithRequisities.jobDetailDbModel.sum
            )
        }
        return result
    }

    fun fromDbToEntity(jobDetailWithRequisities: JobDetailWithRequisities): JobDetail {
            return JobDetail(
                id = jobDetailWithRequisities.jobDetailDbModel.id,
                carJob = carJobMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.carJob),
                quantity = jobDetailWithRequisities.jobDetailDbModel.quantity,
                timeNorm = jobDetailWithRequisities.jobDetailDbModel.timeNorm,
                workingHour = workingHourMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.workingHour),
                sum = jobDetailWithRequisities.jobDetailDbModel.sum
            )
    }

    fun fromListDbToListEntityWithNull(list: List<JobDetailWithRequisities?>) = list.map {
        fromDbToEntityWithNull(it)
    }

    fun fromListDbToListEntity(list: List<JobDetailWithRequisities>) = list.map {
        fromDbToEntity(it)
    }

}