package ru.internetcloud.workorderapplication.presentation.workorder.detail.jobdetails

import ru.internetcloud.workorderapplication.domain.model.document.JobDetail

interface JobDetailListListener {

    fun onClickJobDetail(jobDetail: JobDetail)
}
