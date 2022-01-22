package ru.internetcloud.workorderapplication.domain.document

import java.util.Date
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

data class WorkOrder(
    var id: String = "",
    var number: String = "", // номер документа
    var date: Date = Date(), // дата документа
    var partner: Partner? = null, // заказчик
    var posted: Boolean = false, // проведен
    var car: Car? = null, // схт
    var repairType: RepairType? = null, // вид ремонта
    var department: Department? = null, // цех
    var requestReason: String = "", // причина обращения
    var master: Employee? = null, // мастер (бригадир)
    var comment: String = "", // комментарий
    var mileage: Int = 0, // Пробег (наработка)
    var isNew: Boolean = false,
    var isModified: Boolean = false,
    var performers: List<PerformerDetail> = emptyList(), // исполнители (табличная часть)
    var performersString: String = "", // исполнители строкой
    var jobDetails: MutableList<JobDetail> = mutableListOf() // emptyList() // Работы (табличная часть)
)
