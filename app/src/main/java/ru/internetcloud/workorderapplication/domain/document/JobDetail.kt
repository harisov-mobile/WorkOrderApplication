package ru.internetcloud.workorderapplication.domain.document

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

// это строка табличной части "Работы"
@Parcelize
data class JobDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO,
    var timeNorm: BigDecimal = BigDecimal.ZERO,
    var workingHour: WorkingHour? = null, // нормо-часы
    var sum: BigDecimal = BigDecimal.ZERO,
    var isSelected: Boolean = false
) : Parcelable
