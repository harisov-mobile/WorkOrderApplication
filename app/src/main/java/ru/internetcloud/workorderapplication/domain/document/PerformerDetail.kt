package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.Employee

// это строка табличной части "Исполнители"
data class PerformerDetail(
    var id: String = "",
    var lineNumber: Int = 0,
    val employee: Employee? = null,
    var isSelected: Boolean = false
)
