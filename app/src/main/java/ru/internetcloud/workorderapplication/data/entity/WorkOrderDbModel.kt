package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "work_orders")
data class WorkOrderDbModel(
    @PrimaryKey
    var id: String = "",
    var number: String = "", // номер документа
    var date: Date = Date(), // дата документа
    var partnerId: String = "", // заказчик
    var carId: String = "", // схт
    var mileage: Int = 0, // наработка
    var repairTypeId: String = "", // вид ремонта
    var departmentId: String = "", // цех
    var requestReason: String = "", // причина обращения
    var masterId: String = "", // мастер (бригадир)
    var comment: String = "", // комментарий
    var new: Boolean = false // такого реквизита в 1С нет, это для внутр нужд
    //var performers: List<Employee> = emptyList(), // исполнители (табличная часть)
    //var jobs: List<JobDetail> = emptyList() // Работы (табличная часть)
)
