package ru.internetcloud.workorderapplication.common.domain.model.document

import android.os.Parcelable
import java.util.Date
import kotlinx.parcelize.Parcelize
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Car
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Department
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Employee
import ru.internetcloud.workorderapplication.common.domain.model.catalog.Partner
import ru.internetcloud.workorderapplication.common.domain.model.catalog.RepairType

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
