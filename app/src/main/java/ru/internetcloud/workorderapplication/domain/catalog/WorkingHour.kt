package ru.internetcloud.workorderapplication.domain.catalog

import java.math.BigDecimal

data class WorkingHour(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var price: BigDecimal = BigDecimal.ZERO
)
