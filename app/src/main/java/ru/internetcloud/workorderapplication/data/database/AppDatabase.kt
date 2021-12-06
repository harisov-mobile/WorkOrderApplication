package ru.internetcloud.workorderapplication.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import ru.internetcloud.workorderapplication.data.entity.WorkOrderDbModel

@Database(entities = [WorkOrderDbModel::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun workOrderDao() : WorkOrderDao

}