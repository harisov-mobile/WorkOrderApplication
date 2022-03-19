package ru.internetcloud.workorderapplication.domain.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

@Parcelize
data class DefaultRepairTypeJobDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var carModel: CarModel? = null,
    var carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO

// это надо будет для DbModel ???
//    var repairTypeId: String = "",
//    var modelId: String = "",
//    var carJobId: String = "",
//    var quantity: BigDecimal = BigDecimal.ZERO
) : Parcelable
