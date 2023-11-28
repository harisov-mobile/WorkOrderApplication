package ru.internetcloud.workorderapplication.domain.model.document

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.domain.model.catalog.RepairType

//data class WorkOrder(
//    var id: String = "",
//    var number: String = "", // номер документа
//    var date: Date = Date(), // дата документа
//    var partner: Partner? = null, // заказчик
//    var posted: Boolean = false, // проведен
//    var car: Car? = null, // схт
//    var repairType: RepairType? = null, // вид ремонта
//    var department: Department? = null, // цех
//    var requestReason: String = "", // причина обращения
//    var master: Employee? = null, // мастер (бригадир)
//    var comment: String = "", // комментарий
//    var mileage: Int = 0, // Пробег (наработка)
//    var isNew: Boolean = false,
//    var isModified: Boolean = false,
//    var performers: MutableList<PerformerDetail> = mutableListOf(), // исполнители (табличная часть)
//    var performersString: String = "", // исполнители строкой
//    var jobDetails: MutableList<JobDetail> = mutableListOf() // Работы (табличная часть)
//)

@Parcelize
data class WorkOrder(
    val id: String = "",
    val number: String = "", // номер документа
    val date: Date = Date(), // дата документа
    val partner: Partner? = null, // заказчик
    val posted: Boolean = false, // проведен
    val car: Car? = null, // схт
    val repairType: RepairType? = null, // вид ремонта
    val department: Department? = null, // цех
    val requestReason: String = "", // причина обращения
    val master: Employee? = null, // мастер (бригадир)
    val comment: String = "", // комментарий
    val mileage: Int = 0, // Пробег (наработка)
    val isNew: Boolean = false,
    val isModified: Boolean = false, // понадобится при выгрузке данных в 1С - только isModified и isNew выгружаются
    val performers: List<PerformerDetail> = listOf(), // исполнители (табличная часть)
    val performersString: String = "", // исполнители строкой
    val jobDetails: List<JobDetail> = listOf() // Работы (табличная часть)
) : Parcelable {
    companion object {
        const val EMPTY_ID = ""
    }
}

