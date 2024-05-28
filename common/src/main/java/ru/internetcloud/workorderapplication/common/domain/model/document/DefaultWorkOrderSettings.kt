package ru.internetcloud.workorderapplication.common.domain.model.document

import java.math.BigDecimal
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.common.domain.model.catalog.WorkingHour

data class DefaultWorkOrderSettings(
    var department: Department? = null, // цех
    var employee: Employee? = null, // сам сотрудник
    var master: Employee? = null, // мастер (бригадир)
    var workingHour: WorkingHour? = null, // д.б. нормо-час = "Рубль"
    var defaultTimeNorm: BigDecimal = BigDecimal.ONE
)
