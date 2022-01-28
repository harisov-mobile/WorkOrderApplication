package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee

data class DefaultWorkOrderSettings(
    var department: Department? = null, // цех
    var master: Employee? = null // мастер (бригадир)
)
