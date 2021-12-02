package ru.internetcloud.workorderapplication.domain.document

import java.math.BigDecimal
import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour

// это строка табличной части "Работы"

data class JobDetail(
    var id: String = "",
    val carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO,
    var timeNorm: BigDecimal = BigDecimal.ZERO,
    var workingHour: WorkingHour? = null,
    var sum: BigDecimal = BigDecimal.ZERO
)
