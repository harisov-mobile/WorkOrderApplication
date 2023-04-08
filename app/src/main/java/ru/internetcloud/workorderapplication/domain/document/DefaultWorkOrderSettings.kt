package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.catalog.WorkingHour
import java.math.BigDecimal

data class DefaultWorkOrderSettings(
    var department: Department? = null, // цех
    var employee: Employee? = null, // сам сотрудник
    var master: Employee? = null, // мастер (бригадир)
    var workingHour: WorkingHour? = null, // д.б. нормо-час = "Рубль"
    var defaultTimeNorm: BigDecimal = BigDecimal.ONE
)
