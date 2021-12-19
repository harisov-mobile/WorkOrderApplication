package ru.internetcloud.workorderapplication.data.entity

import androidx.room.Entity

@Entity(tableName = "work_orders_and_job_details", primaryKeys = ["workOrderId", "jobDetailId"])
data class WorkOrderAndJobDetailCrossRef(
    val workOrderId: String,
    val jobDetailId: String
)