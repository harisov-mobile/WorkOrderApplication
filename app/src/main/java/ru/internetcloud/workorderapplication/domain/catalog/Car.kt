package ru.internetcloud.workorderapplication.domain.catalog

// спр-к Автомобили (СХТ) в 1С
data class Car(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var vin: String = "",
    var manufacturer: String = "", // производитель
    var model: String = "",
    var type: String = "", // папка в спр-ке "Модели"
    var releaseDate: Int = 0, // год выпуска
    var mileage: Int = 0, // пробег
    var owner: Partner? = null // владелец
)
