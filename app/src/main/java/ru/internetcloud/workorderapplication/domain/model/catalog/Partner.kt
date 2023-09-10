package ru.internetcloud.workorderapplication.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Контрагенты
@Parcelize
data class Partner(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var fullName: String = "",
    var inn: String = "",
    var kpp: String = "",
    var isSelected: Boolean = false
) : Parcelable
