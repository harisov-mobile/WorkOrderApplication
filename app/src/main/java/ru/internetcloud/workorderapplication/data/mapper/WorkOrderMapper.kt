package ru.internetcloud.workorderapplication.data.mapper

import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel
import ru.internetcloud.workorderapplication.data.entity.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.data.network.dto.WorkOrderDTO
import ru.internetcloud.workorderapplication.domain.common.DateConverter
import ru.internetcloud.workorderapplication.domain.document.WorkOrder

class WorkOrderMapper {

    private val partnerMapper = PartnerMapper()
    private val carMapper = CarMapper()
    private val repairTypeMapper = RepairTypeMapper()
    private val departmentMapper = DepartmentMapper()
    private val employeeMapper = EmployeeMapper()
    private val performerDetailMapper = PerformerDetailMapper()
    private val jobDetailMapper = JobDetailMapper()

    fun fromEntityToDbModel(workOrder: WorkOrder): WorkOrderDbModel {
        return WorkOrderDbModel(
            id = workOrder.id,
            number = workOrder.number,
            date = workOrder.date
        )
    }

    fun fromDbModelToEntity(workOrderWithDetails: WorkOrderWithDetails): WorkOrder {
        return WorkOrder(
            id = workOrderWithDetails.workOrder.id,
            number = workOrderWithDetails.workOrder.number,
            date = workOrderWithDetails.workOrder.date,
            partner = partnerMapper.fromDbModelToEntityWithNull(workOrderWithDetails.partner),
            car = carMapper.fromDbModelToEntityWithNull(workOrderWithDetails.car),
            repairType = repairTypeMapper.fromDbModelToEntityWithNull(workOrderWithDetails.repairType),
            department = departmentMapper.fromDbModelToEntityWithNull(workOrderWithDetails.department),
            requestReason = workOrderWithDetails.workOrder.requestReason,
            master = employeeMapper.fromDbModelToEntityWithNull(workOrderWithDetails.master),
            comment = workOrderWithDetails.workOrder.comment,
            performers = performerDetailMapper.fromListDbToListEntity(workOrderWithDetails.performers),
            jobDetails = jobDetailMapper.fromListDbToListEntity(workOrderWithDetails.jobDetails)
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
            // date = Date(),
            partnerId = workOrderDTO.partnerId,
            carId = workOrderDTO.carId,
            repairTypeId = workOrderDTO.repairTypeId,
            departmentId = workOrderDTO.departmentId,
            requestReason = workOrderDTO.requestReason,
            masterId = workOrderDTO.masterId,
            comment = workOrderDTO.comment
        )
    }
}
