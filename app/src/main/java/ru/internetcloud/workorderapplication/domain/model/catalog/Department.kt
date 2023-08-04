package ru.internetcloud.workorderapplication.domain.model.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Цеха
@Parcelize
data class Department(
    var id: String = "",
    var code1C: String = "",
    var name: String = "",
    var isSelected: Boolean = false
) : Parcelable
