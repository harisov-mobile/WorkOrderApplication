package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.network.dto.JobDetailDTO

class JobDetailMapper {

    fun fromDtoToDbModel(jobDetailDTO: JobDetailDTO): JobDetailDbModel {
        return JobDetailDbModel(
            id = jobDetailDTO.id,
            carJobId = jobDetailDTO.carJobId,
            quantity = jobDetailDTO.quantity,
            timeNorm = jobDetailDTO.timeNorm,
            workingHourId = jobDetailDTO.workingHourId,
            sum = jobDetailDTO.sum
        )
    }

}