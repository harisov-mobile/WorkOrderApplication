package ru.internetcloud.workorderapplication.presentation.workorder.detail

import java.util.Date
import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType
import ru.internetcloud.workorderapplication.domain.model.document.JobDetail
import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail

sealed interface WorkOrderDetailEvent {

    data class OnNumberChange(val number: String) : WorkOrderDetailEvent

    data class OnEmailChange(val email: String) : WorkOrderDetailEvent

    data class OnMileageChange(val mileage: Int) : WorkOrderDetailEvent

    data class OnRequestReasonChange(val requestReason: String) : WorkOrderDetailEvent

    data class OnCommentChange(val comment: String) : WorkOrderDetailEvent

    data class OnDateChange(val date: Date) : WorkOrderDetailEvent

    data class OnPartnerChange(val partner: Partner?) : WorkOrderDetailEvent

    data class OnCarChange(val car: Car?) : WorkOrderDetailEvent

    data class OnRepairTypeChange(val repairType: RepairType?) : WorkOrderDetailEvent

    data class OnDepartmentChange(val department: Department?) : WorkOrderDetailEvent

    data class OnMasterChange(val master: Employee?) : WorkOrderDetailEvent

    data class OnJobDetailChange(val jobDetail: JobDetail?) : WorkOrderDetailEvent

    data class OnPerformerDetailChange(val performerDetail: PerformerDetail?) : WorkOrderDetailEvent

    data class OnSelectPerformerChange(val performerDetail: PerformerDetail) : WorkOrderDetailEvent

    data class OnSelectJobChange(val jobDetail: JobDetail) : WorkOrderDetailEvent

    data class OnSave(val shouldCloseScreen: Boolean) : WorkOrderDetailEvent

    object OnJobDetailDelete : WorkOrderDetailEvent

    object OnFillDefaultJobs : WorkOrderDetailEvent

    object OnResetFillDefaultJobs : WorkOrderDetailEvent

    object OnPerformerDetailDelete : WorkOrderDetailEvent

    object OnIncorrectEmail : WorkOrderDetailEvent

}
