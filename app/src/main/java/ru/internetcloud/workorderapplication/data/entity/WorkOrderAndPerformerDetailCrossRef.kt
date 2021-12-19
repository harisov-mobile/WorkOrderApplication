package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity

@Entity(tableName = "work_orders_and_performer_details", primaryKeys = ["workOrderId", "performerDetailId"])
data class WorkOrderAndPerformerDetailCrossRef(
    val workOrderId: String,
    val performerDetailId: String
)
