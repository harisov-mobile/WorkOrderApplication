package ru.internetcloud.workorderapplication.domain.document

import ru.internetcloud.workorderapplication.domain.catalog.*
import java.util.*

data class WorkOrder(
    var id: String = "",
    var number: String = "",
    var date: Date,
    var client: Partner? = null,
    var car: Car? = null,
    var mileage: Int = 0,
    var repairType: RepairType? = null,
    var department: Department? = null,
    var requestReason: String? = null,
    var master: Employee? = null,
    var Comment: String? = null,
    var performers: List<Employee>,
    var jobs: List<JobDetail>
)
