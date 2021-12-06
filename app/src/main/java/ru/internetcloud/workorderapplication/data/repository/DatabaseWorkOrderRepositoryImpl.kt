package ru.internetcloud.workorderapplication.data.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.room.Room
import ru.internetcloud.workorderapplication.data.database.AppDatabase
import ru.internetcloud.workorderapplication.domain.document.WorkOrder
import ru.internetcloud.workorderapplication.domain.repository.WorkOrderRepository
import java.util.*

// это класс-синглтон, единственный экземпляр этого репозитория создается при запуске приложения
class DatabaseWorkOrderRepositoryImpl private constructor(application: Application): WorkOrderRepository {

    private val database: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java,
        DATABASE_NAME
    ).build()

    companion object {
        private const val DATABASE_NAME = "work_order.db"

        private var instance: DatabaseWorkOrderRepositoryImpl? = null

        fun initialize(application: Application) {
            if (instance == null) {
                instance = DatabaseWorkOrderRepositoryImpl(application)
            }
        }

        fun get(): DatabaseWorkOrderRepositoryImpl {
            return instance ?: throw RuntimeException("DatabaseWorkOrderRepositoryImpl must be initialized.")
        }
    }

    override fun addWorkOrder(workOrder: WorkOrder) {
        TODO("Not yet implemented")
    }

    override fun updateWorkOrder(workOrder: WorkOrder) {
        TODO("Not yet implemented")
    }

    override fun getWorkOrderList(): LiveData<List<WorkOrder>> {
        TODO("Not yet implemented")
    }

    override fun getWorkOrder(id: UUID): WorkOrder? {
        TODO("Not yet implemented")
    }
}