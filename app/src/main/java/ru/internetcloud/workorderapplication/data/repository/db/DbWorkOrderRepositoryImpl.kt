package ru.internetcloud.workorderapplication.data.repository.db

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import androidx.sqlite.db.SimpleSQLiteQuery
import ru.internetcloud.workorderapplication.data.database.AppDao
import ru.internetcloud.workorderapplication.data.mapper.JobDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.PerformerDetailMapper
import ru.internetcloud.workorderapplication.data.mapper.WorkOrderMapper
import ru.internetcloud.workorderapplication.domain.common.SearchWorkOrderData
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import java.util.*
import javax.inject.Inject


class DbWorkOrderRepositoryImpl @Inject constructor(
    private val workOrderDao: AppDao,
    private val workOrderMapper: WorkOrderMapper,
    private val jobDetailMapper: JobDetailMapper,
    private val performerDetailMapper: PerformerDetailMapper
) : WorkOrderRepository {

    override suspend fun updateWorkOrder(workOrder: WorkOrder) {
        // т.к. onConflict = OnConflictStrategy.REPLACE, то это будет и UPDATE тоже
        workOrderDao.addWorkOrder(workOrderMapper.fromEntityToDbModel(workOrder))

        // сначала удалим ТЧ Работы, относящиеся к данному ордеру
        workOrderDao.deleteJobDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Работы
        workOrderDao.addJobDetailList(
            jobDetailMapper.fromListEntityToListDbModel(
                list = workOrder.jobDetails,
                workOrderId = workOrder.id
            )
        )

        // сначала удалим ТЧ Исполнители, относящиеся к данному ордеру
        workOrderDao.deletePerformersDetailsByWorkOrder(workOrder.id)
        // потом перезапишем Исполнители
        workOrderDao.addPerformerDetailList(
            performerDetailMapper.fromListEntityToListDbModel(
                list = workOrder.performers,
                workOrderId = workOrder.id
            )
        )
    }

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        return Transformations.map(workOrderDao.getWorkOrderList()) {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    override fun getFilteredWorkOrderList(searchWorkOrderData: SearchWorkOrderData): LiveData<List<WorkOrder>> {

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

        return Transformations.map(workOrderDao.getFilteredWorkOrderList(query)) {
            workOrderMapper.fromListDbModelToListEntity(it)
        }
    }

    override suspend fun getWorkOrder(workOrderId: String): WorkOrder? {
        var workOrder: WorkOrder? = null

        val workOrderWithDetails = workOrderDao.getWorkOrder(workOrderId)

        workOrderWithDetails?.let {
            workOrder = workOrderMapper.fromDbModelToEntity(it)
        }

        return workOrder
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
}
