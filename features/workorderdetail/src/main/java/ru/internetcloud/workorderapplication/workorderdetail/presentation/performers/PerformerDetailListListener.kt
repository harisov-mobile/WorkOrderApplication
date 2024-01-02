package ru.internetcloud.workorderapplication.workorderdetail.presentation.performers

import ru.internetcloud.workorderapplication.common.domain.model.document.PerformerDetail

interface PerformerDetailListListener {

    fun onClickPerformerDetail(performerDetail: PerformerDetail)
}
