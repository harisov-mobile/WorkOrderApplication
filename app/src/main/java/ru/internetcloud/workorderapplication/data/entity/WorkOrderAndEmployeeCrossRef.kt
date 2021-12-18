package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity

@Entity(tableName = "work_orders_and_employees", primaryKeys = ["workOrderId", "employeeId"])
data class WorkOrderAndEmployeeCrossRef(
    val workOrderId: String,
    val employeeId: String
)
