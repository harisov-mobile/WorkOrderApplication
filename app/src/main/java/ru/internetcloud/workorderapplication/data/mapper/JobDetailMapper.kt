package ru.internetcloud.workorderapplication.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.data.model.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.model.JobDetailWithRequisities
import ru.internetcloud.workorderapplication.data.network.dto.JobDetailDTO
import ru.internetcloud.workorderapplication.domain.document.JobDetail

class JobDetailMapper @Inject constructor(
    private val carJobMapper: CarJobMapper,
    private val workingHourMapper: WorkingHourMapper
) {

    fun fromDtoToDbModel(jobDetailDTO: JobDetailDTO): JobDetailDbModel {
        return JobDetailDbModel(
            id = jobDetailDTO.id,
            lineNumber = jobDetailDTO.lineNumber,
            carJobId = jobDetailDTO.carJobId,
            quantity = jobDetailDTO.quantity,
            timeNorm = jobDetailDTO.timeNorm,
            workingHourId = jobDetailDTO.workingHourId,
            sum = jobDetailDTO.sum,
            workOrderId = jobDetailDTO.workOrderId
        )
    }

    fun fromDbToEntity(jobDetailWithRequisities: JobDetailWithRequisities): JobDetail {
        return JobDetail(
            id = jobDetailWithRequisities.jobDetailDbModel.id,
            lineNumber = jobDetailWithRequisities.jobDetailDbModel.lineNumber,
            carJob = carJobMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.carJob),
            quantity = jobDetailWithRequisities.jobDetailDbModel.quantity,
            timeNorm = jobDetailWithRequisities.jobDetailDbModel.timeNorm,
            workingHour = workingHourMapper.fromDbModelToEntityWithNull(jobDetailWithRequisities.workingHour),
            sum = jobDetailWithRequisities.jobDetailDbModel.sum
        )
    }

    fun fromListDbToListEntity(list: List<JobDetailWithRequisities>): MutableList<JobDetail> {
        val sortedList = list.sortedBy {
            it.jobDetailDbModel.lineNumber
        }

        val result: MutableList<JobDetail> = mutableListOf()

        sortedList.forEach {
            result.add(fromDbToEntity(it))
        }

        return result
    }

    fun fromListEntityToListDbModel(list: List<JobDetail>, workOrderId: String) = list.map {
        fromEntityToDbModel(it, workOrderId)
    }

    fun fromEntityToDbModel(jobDetail: JobDetail, workOrderId: String): JobDetailDbModel {
        return JobDetailDbModel(
            id = jobDetail.id,
            lineNumber = jobDetail.lineNumber,
            carJobId = jobDetail.carJob?.id ?: "",
            quantity = jobDetail.quantity,
            timeNorm = jobDetail.timeNorm,
            workingHourId = jobDetail.workingHour?.id ?: "",
            sum = jobDetail.sum,
            workOrderId = workOrderId
        )
    }

    fun fromListDtoToDbModel(list: List<JobDetailDTO>): List<JobDetailDbModel> {
        return list.map { fromDtoToDbModel(it) }
    }
}
