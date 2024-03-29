package ru.internetcloud.workorderapplication.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal

// спр-к Нормочасы
@Parcelize
data class WorkingHour(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO,
    var isSelected: Boolean = false
) : Parcelable
