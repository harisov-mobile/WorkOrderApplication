package ru.internetcloud.workorderapplication.data.datasource.local

import androidx.sqlite.db.SimpleSQLiteQuery
import java.util.Calendar
import java.util.Date
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.data.model.JobDetailDbModel
import ru.internetcloud.workorderapplication.data.model.PerformerDetailDbModel
import ru.internetcloud.workorderapplication.data.model.WorkOrderDbModel
import ru.internetcloud.workorderapplication.data.model.WorkOrderWithDetails
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.model.document.WorkOrder

class WorkOrderLocalDataSource @Inject constructor(
    private val appDao: AppDao,
    private val workOrderMapper: WorkOrderMapper,
    private val jobDetailMapper: JobDetailMapper,
    private val performerDetailMapper: PerformerDetailMapper
) {

    suspend fun updateWorkOrder(workOrder: WorkOrder) {
        // т.к. onConflict = OnConflictStrategy.REPLACE, то это будет и UPDATE тоже
        appDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))

        // сначала удалим ТЧ Работы, относящиеся к данному ордеру
        appDao.deleteJobDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Работы
        appDao.addJobDetailList(
            jobDetailMapper.fromListEntityToListDbModel(
                list = workOrder.jobDetails,
                workOrderId = workOrder.id
            )
        )

        // сначала удалим ТЧ Исполнители, относящиеся к данному ордеру
        appDao.deletePerformersDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Исполнители
        appDao.addPerformerDetailList(
            performerDetailMapper.fromListEntityToListDbModel(
                list = workOrder.performers,
                workOrderId = workOrder.id
            )
        )
    }

    fun getWorkOrderList(): Flow<List<WorkOrder>> {
        return appDao.getWorkOrderList().map {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): Flow<List<WorkOrder>> {
        // Query string
        var queryString = String()

        // List of bind parameters
        val args: MutableList<Any> = ArrayList()

        var containsCondition = false

        // Beginning of query string
        queryString += "SELECT * FROM work_orders"

        // Optional parts are added to query string and to args upon here
        if (searchWorkOrderData.numberText.isNotEmpty()) {
            queryString += " WHERE"
            queryString += " number LIKE :param_num"
            args.add("%" + searchWorkOrderData.numberText + "%")
            containsCondition = true
        }

        if (searchWorkOrderData.partnerText.isNotEmpty()) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " partnerId IN (SELECT id FROM partners WHERE partners.name LIKE :param_partner)"
            args.add("%" + searchWorkOrderData.partnerText + "%")
            containsCondition = true
        }

        if (searchWorkOrderData.carText.isNotEmpty()) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " carId IN (SELECT id FROM cars WHERE cars.name LIKE :param_car)"
            args.add("%" + searchWorkOrderData.carText + "%")
            containsCondition = true
        }

        if (searchWorkOrderData.performerText.isNotEmpty()) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " id IN " +
                " (SELECT workOrderId FROM performer_details" +
                " WHERE performer_details.employeeId IN" +
                " (SELECT id FROM employees WHERE employees.name LIKE :param_performer))"
            args.add("%" + searchWorkOrderData.performerText + "%")
            containsCondition = true
        }

        if (searchWorkOrderData.departmentText.isNotEmpty()) {
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " departmentId IN (SELECT id FROM departments WHERE departments.name LIKE :param_department)"
            args.add("%" + searchWorkOrderData.departmentText + "%")
            containsCondition = true
        }

        searchWorkOrderData.dateFrom?.let { fromDate ->
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " date >= :param_date_from"
            args.add(fromDate.getTime())
        }

        searchWorkOrderData.dateTo?.let { toDate ->
            if (containsCondition) {
                queryString += " AND"
            } else {
                queryString += " WHERE"
                containsCondition = true
            }
            queryString += " date <= :param_date_to"
            args.add(getEndOfDay(toDate).getTime())
        }

        // End of query string
        queryString += " ORDER BY date, number;"

        // val query = SimpleSQLiteQuery(queryString, args.toTypedArray())
        val query = SimpleSQLiteQuery(queryString, args.toTypedArray())

        return appDao.getFilteredWorkOrderList(query).map {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        var workOrder: WorkOrder? = null
        val workOrderWithDetails = appDao.getWorkOrder(workOrderId)
        workOrderWithDetails?.let {
            workOrder = workOrderMapper.fromDbModelToEntity(it)
        }
        return workOrder
    }

    suspend fun getDuplicateWorkOrderByNumber(number: String, workOrderId: String): WorkOrder? {
        var workOrder: WorkOrder? = null
        val workOrderDbModel = appDao.getDuplicateWorkOrderByNumber(number = number, workOrderId = workOrderId)
        workOrderDbModel?.let {
            workOrder = workOrderMapper.fromDbModelToEntity(it)
        }
        return workOrder
    }

    suspend fun getModifiedWorkOrdersQuantity(): Int {
        return appDao.getModifiedWorkOrders().size
    }

    suspend fun getModifiedWorkOrders() = appDao.getModifiedWorkOrders()

    suspend fun getModifiedWorkOrderById(id: String): WorkOrderWithDetails? {
        return appDao.getModifiedWorkOrderById(id)
    }

    fun getEndOfDay(date: Date): Date {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar[Calendar.HOUR_OF_DAY] = 23
        calendar[Calendar.MINUTE] = 59
        calendar[Calendar.SECOND] = 59
        calendar[Calendar.MILLISECOND] = 999
        return calendar.time
    }

    suspend fun deleteAllJobDetails() {
        appDao.deleteAllJobDetails()
    }

    suspend fun deleteAllPerformers() {
        appDao.deleteAllPerformers()
    }

    suspend fun deleteAllWorkOrders() {
        appDao.deleteAllWorkOrders()
    }

    suspend fun addJobDetailList(jobDetailDbModelList: List<JobDetailDbModel>) {
        appDao.addJobDetailList(jobDetailDbModelList)
    }

    suspend fun addPerformerDetailList(performerDetailDbModelList: List<PerformerDetailDbModel>) {
        appDao.addPerformerDetailList(performerDetailDbModelList)
    }

    suspend fun addWorkOrderList(workOrderDbModelList: List<WorkOrderDbModel>) {
        appDao.addWorkOrderList(workOrderDbModelList)
    }
}
