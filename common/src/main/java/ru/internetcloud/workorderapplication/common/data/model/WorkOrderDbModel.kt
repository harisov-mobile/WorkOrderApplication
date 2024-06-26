package ru.internetcloud.workorderapplication.common.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "work_orders")
data class WorkOrderDbModel(
    @PrimaryKey
    var id: String = "",
    var number: String = "", // номер документа
    var date: Date = Date(), // дата документа
    var posted: Boolean = false, // проведен
    var partnerId: String = "", // заказчик
    var carId: String = "", // схт
    var repairTypeId: String = "", // вид ремонта
    var departmentId: String = "", // цех
    var requestReason: String = "", // причина обращения
    var masterId: String = "", // мастер (бригадир)
    var comment: String = "", // комментарий
    var performersString: String = "", // исполнители строкой
    var mileage: Int = 0, // Пробег (наработка)
    var isNew: Boolean = false,
    var isModified: Boolean = false
)
