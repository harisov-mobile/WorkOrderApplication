package ru.internetcloud.workorderapplication.domain.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Автоработы в 1С

@Parcelize
data class CarJob(
    val id: String = "",
    val code1C: String = "",
    val name: String = "",
    val folder: String = "",
    var isSelected: Boolean = false
) : Parcelable
