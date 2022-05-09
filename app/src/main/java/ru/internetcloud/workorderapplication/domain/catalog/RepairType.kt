package ru.internetcloud.workorderapplication.domain.catalog

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

// спр-к Виды ремонта
@Parcelize
data class RepairType(
    var id: String = "",
    var name: String = "",
    var defaultJobDetails: MutableList<DefaultRepairTypeJobDetail> = mutableListOf(), // Работы по-умолчанию (табличная часть)
    var isSelected: Boolean = false
) : Parcelable
