package ru.internetcloud.workorderapplication.common.domain.model.catalog

import android.os.Parcelable
import java.math.BigDecimal
import kotlinx.parcelize.Parcelize

// спр-к Нормочасы
@Parcelize
data class WorkingHour(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var isSelected: Boolean = false
) : Parcelable
