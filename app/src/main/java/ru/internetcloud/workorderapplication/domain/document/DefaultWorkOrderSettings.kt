package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee

data class DefaultWorkOrderSettings(
    var department: Department? = null, // цех
    var employee: Employee? = null, // сам сотрудник
    var master: Employee? = null // мастер (бригадир)
)
