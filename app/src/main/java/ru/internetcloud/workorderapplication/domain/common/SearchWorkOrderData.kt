package ru.internetcloud.workorderapplication.domain.common

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize

@Parcelize
data class SearchWorkOrderData(
    var numberText: String = "",
    var partnerText: String = "",
    var carText: String = "",
    var performerText: String = "",
    var departmentText: String = "",
    var dateFrom: Date? = null,
    var dateTo: Date? = null
) : Parcelable
