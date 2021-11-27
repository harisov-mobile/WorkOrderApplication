package ru.internetcloud.workorderapplication.domain.document

import java.util.Date
import java.util.UUID
import ru.internetcloud.workorderapplication.domain.catalog.Car
import ru.internetcloud.workorderapplication.domain.catalog.Department
import ru.internetcloud.workorderapplication.domain.catalog.Employee
import ru.internetcloud.workorderapplication.domain.catalog.Partner
import ru.internetcloud.workorderapplication.domain.catalog.RepairType

data class WorkOrder(
    val id: UUID = UUID.randomUUID(),
    var number: String = "",
    var date: Date = Date(),
    var client: Partner? = null,
    var car: Car? = null,
    var mileage: Int = 0,
    var repairType: RepairType? = null,
    var department: Department? = null,
    var requestReason: String? = null,
    var master: Employee? = null,
    var Comment: String? = null,
    var performers: List<Employee> = emptyList(),
    var jobs: List<JobDetail> = emptyList()
)
