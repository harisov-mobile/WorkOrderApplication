package ru.internetcloud.workorderapplication.data.database

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.internetcloud.workorderapplication.data.entity.*

@Database(
    entities = [WorkOrderDbModel::class, RepairTypeDbModel::class, CarJobDbModel::class, DepartmentDbModel::class,
        EmployeeDbModel::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    companion object {

        private const val DATABASE_NAME = "work_order.db"
        private var Instance: AppDatabase? = null

        private val Lock = Any()

        fun getInstance(application: Application): AppDatabase {
            Instance?.let {
                return it
            }
            synchronized(Lock) {
                Instance?.let {
                    return it
                }
                val db = Room.databaseBuilder(
                    application,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    .build()
                Instance = db
                return db
            }
        }
    }

    abstract fun appDao(): AppDao
}
