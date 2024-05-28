package ru.internetcloud.workorderapplication.common.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CarModel(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var isSelected: Boolean = false
) : Parcelable
