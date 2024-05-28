package ru.internetcloud.workorderapplication.navigationimpl.mapper

import javax.inject.Inject
import ru.internetcloud.workorderapplication.common.domain.common.mapper.Mapper
import ru.internetcloud.workorderapplication.workorderdetail.presentation.navigation.WorkOrderDetailArgs
import ru.internetcloud.workorderapplication.workorders.presentation.navigation.ToWorkOrderArgs

class WorkOrderDetailArgsMapper @Inject constructor() : Mapper<ToWorkOrderArgs, WorkOrderDetailArgs> {

    override fun map(item: ToWorkOrderArgs): WorkOrderDetailArgs {
        return WorkOrderDetailArgs(
            workOrderId = item.workOrderId,
            editMode = item.editMode,
            requestKeyReturnResult = item.requestKeyReturnResult,
            argNameReturnResult = item.argNameReturnResult
        )
    }
}
