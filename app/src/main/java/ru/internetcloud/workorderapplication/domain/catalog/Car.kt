package ru.internetcloud.workorderapplication.domain.catalog

import java.util.Date

data class Car(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var vin: String = "",
    var manufacturer: Partner? = null,
    var model: CarModel? = null,
    var releaseDate: Date? = null,
    var mileage: Int = 0,
    var owner: Partner? = null
)
