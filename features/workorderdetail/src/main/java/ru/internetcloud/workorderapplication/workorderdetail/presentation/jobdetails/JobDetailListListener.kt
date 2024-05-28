package ru.internetcloud.workorderapplication.workorderdetail.presentation.jobdetails

import ru.internetcloud.workorderapplication.common.domain.model.document.JobDetail

interface JobDetailListListener {

    fun onClickJobDetail(jobDetail: JobDetail)
}
