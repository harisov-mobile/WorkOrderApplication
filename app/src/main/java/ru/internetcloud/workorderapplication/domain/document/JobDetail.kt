package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.CarJob
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import java.math.BigDecimal

data class JobDetail(
    var id: String = "",
    val carJob: CarJob? = null,
    var quantity: BigDecimal = BigDecimal.ZERO,
    var timeNorm: BigDecimal = BigDecimal.ZERO,
    var workingHour: WorkingHour? = null,
    var sum: BigDecimal = BigDecimal.ZERO,
)
