package ru.internetcloud.workorderapplication.common.domain.model.catalog

import android.os.Parcelable
import java.math.BigDecimal
import kotlinx.parcelize.Parcelize

@Parcelize
data class DefaultRepairTypeJobDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var carModel: CarModel? = null,
    var carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO
) : Parcelable
