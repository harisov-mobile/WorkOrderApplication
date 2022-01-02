package ru.internetcloud.workorderapplication.domain.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Автомобили (СХТ) в 1С
@Parcelize
data class Car(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var vin: String = "",
    var manufacturer: String = "", // производитель
    var model: String = "",
    var type: String = "", // папка в спр-ке "Модели"
    var releaseYear: Int = 0, // год выпуска
    var mileage: Int = 0, // пробег
    var owner: Partner? = null, // владелец
    var isSelected: Boolean = false
) : Parcelable
