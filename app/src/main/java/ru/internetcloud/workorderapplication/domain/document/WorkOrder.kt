package ru.internetcloud.workorderapplication.domain.document

import java.util.Date
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

data class WorkOrder(
    var id: Int,
    var id1C: String,
    var number: String = "", // номер документа
    var date: Date = Date(), // дата документа
    var client: Partner? = null, // заказчик
    var car: Car? = null, // схт
    var mileage: Int = 0, // наработка
    var repairType: RepairType? = null, // вид ремонта
    var department: Department? = null, // цех
    var requestReason: String? = null, // причина обращения
    var master: Employee? = null, // мастер (бригадир)
    var comment: String? = null, // комментарий
    var performers: List<Employee> = emptyList(), // исполнители (табличная часть)
    var jobs: List<JobDetail> = emptyList() // Работы (табличная часть)
)
