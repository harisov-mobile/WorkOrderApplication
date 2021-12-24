package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.Employee

// это строка табличной части "Исполнители"
data class PerformerDetail(
    var id: String = "",
    val employee: Employee? = null
)
