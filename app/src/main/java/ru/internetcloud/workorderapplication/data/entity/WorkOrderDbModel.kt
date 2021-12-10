package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "work_orders")
data class WorkOrderDbModel(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    var id1C: String = "",
    var number: String = "", // номер документа
    var date: Date = Date(), // дата документа
    // var client: Partner? = null, // заказчик
    // var car: Car? = null, // схт
    var mileage: Int = 0 // наработка
//    var repairType: RepairType? = null, // вид ремонта
//    var department: Department? = null, // цех
//    var requestReason: String? = null, // причина обращения
//    var master: Employee? = null, // мастер (бригадир)
//    var comment: String? = null, // комментарий
//    var performers: List<Employee> = emptyList(), // исполнители (табличная часть)
//    var jobs: List<JobDetail> = emptyList() // Работы (табличная часть)
)
