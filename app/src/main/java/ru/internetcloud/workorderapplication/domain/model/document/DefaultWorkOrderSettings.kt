package ru.internetcloud.workorderapplication.domain.model.document

import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.model.catalog.WorkingHour
import java.math.BigDecimal

data class DefaultWorkOrderSettings(
    var department: Department? = null, // цех
    var employee: Employee? = null, // сам сотрудник
    var master: Employee? = null, // мастер (бригадир)
    var workingHour: WorkingHour? = null, // д.б. нормо-час = "Рубль"
    var defaultTimeNorm: BigDecimal = BigDecimal.ONE
)
