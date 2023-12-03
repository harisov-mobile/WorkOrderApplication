package ru.internetcloud.workorderapplication.presentation.workorder.detail.performers

import ru.internetcloud.workorderapplication.domain.model.document.PerformerDetail

interface PerformerDetailListListener {

    fun onClickPerformerDetail(performerDetail: PerformerDetail)
}
