package ru.internetcloud.workorderapplication.common.data.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.data.model.WorkOrderDbModel
import ru.internetcloud.workorderapplication.common.data.model.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.common.data.network.dto.WorkOrderDTO
import ru.internetcloud.workorderapplication.common.domain.common.DateConverter
import ru.internetcloud.workorderapplication.common.domain.model.document.WorkOrder

class WorkOrderMapper @Inject constructor(
    private val partnerMapper: PartnerMapper,
    private val carMapper: CarMapper,
    private val repairTypeMapper: RepairTypeMapper,
    private val departmentMapper: DepartmentMapper,
    private val employeeMapper: EmployeeMapper,
    private val performerDetailMapper: PerformerDetailMapper,
    private val jobDetailMapper: JobDetailMapper
) {

    fun fromEntityToDbModel(workOrder: WorkOrder): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrder.id,
            number = workOrder.number,
            date = workOrder.date,
            posted = workOrder.posted,
            partnerId = workOrder.partner?.id ?: "",
            carId = workOrder.car?.id ?: "",
            repairTypeId = workOrder.repairType?.id ?: "",
            departmentId = workOrder.department?.id ?: "",
            requestReason = workOrder.requestReason,
            masterId = workOrder.master?.id ?: "",
            comment = workOrder.comment,
            performersString = workOrder.performersString,
            mileage = workOrder.mileage,
            isNew = workOrder.isNew,
            isModified = workOrder.isModified
        )
    }

    fun fromDbModelToEntity(workOrderWithDetails: WorkOrderWithDetails): WorkOrder {
        return WorkOrder(
            id = workOrderWithDetails.workOrder.id,
            number = workOrderWithDetails.workOrder.number,
            date = workOrderWithDetails.workOrder.date,
            posted = workOrderWithDetails.workOrder.posted,
            partner = partnerMapper.fromDbModelToEntityWithNull(workOrderWithDetails.partner),
            car = carMapper.fromCarWithOwnerToEntityWithNull(workOrderWithDetails.car),
            repairType = repairTypeMapper.fromDbModelToEntityWithNull(workOrderWithDetails.repairType),
            department = departmentMapper.fromDbModelToEntityWithNull(workOrderWithDetails.department),
            requestReason = workOrderWithDetails.workOrder.requestReason,
            master = employeeMapper.fromDbModelToEntityWithNull(workOrderWithDetails.master),
            comment = workOrderWithDetails.workOrder.comment,
            performersString = workOrderWithDetails.workOrder.performersString,
            mileage = workOrderWithDetails.workOrder.mileage,
            performers = performerDetailMapper.fromListDbToListEntity(workOrderWithDetails.performers),
            jobDetails = jobDetailMapper.fromListDbToListEntity(workOrderWithDetails.jobDetails),
            isNew = workOrderWithDetails.workOrder.isNew,
            isModified = workOrderWithDetails.workOrder.isModified
        )
    }

    fun fromListDbModelToListEntity(list: List<WorkOrderWithDetails>) = list.map {
        fromDbModelToEntity(it)
    }

    fun fromListDtoToListDboModel(list: List<WorkOrderDTO>) = list.map {
        fromDtoToDbModel(it)
    }

    fun fromDtoToDbModel(workOrderDTO: WorkOrderDTO): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrderDTO.id,
            number = workOrderDTO.number,
            date = DateConverter.fromStringToDate(workOrderDTO.dateString),
            posted = workOrderDTO.posted,
            partnerId = workOrderDTO.partnerId,
            carId = workOrderDTO.carId,
            repairTypeId = workOrderDTO.repairTypeId,
            departmentId = workOrderDTO.departmentId,
            requestReason = workOrderDTO.requestReason,
            masterId = workOrderDTO.masterId,
            comment = workOrderDTO.comment,
            performersString = workOrderDTO.performersString,
            mileage = workOrderDTO.mileage
        )
    }
}
