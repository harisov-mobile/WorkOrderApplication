package ru.internetcloud.workorderapplication.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Сотрудники
@Parcelize
data class Employee(
    var id: String = "",
    var name: String = "",
    var code1C: String = "",
    var isSelected: Boolean = false
) : Parcelable
