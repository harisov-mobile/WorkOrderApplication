package ru.internetcloud.workorderapplication.domain.document

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.domain.catalog.Employee

// это строка табличной части "Исполнители"
@Parcelize
data class PerformerDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    var employee: Employee? = null,
    var isSelected: Boolean = false
) : Parcelable
