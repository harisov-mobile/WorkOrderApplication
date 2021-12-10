package ru.internetcloud.workorderapplication.data.database

import androidx.room.TypeConverter
import java.util.Date

class DatabaseTypeConverters {

    @TypeConverter
    fun fromData(date: Date?): Long? {
        return date?.time
    }

    @TypeConverter
    fun toDate(millisec: Long?): Date? {
        return millisec?.let {
            Date(it)
        }
    }
}
