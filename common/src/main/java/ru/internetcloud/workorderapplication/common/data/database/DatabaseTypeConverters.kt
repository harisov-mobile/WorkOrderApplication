package ru.internetcloud.workorderapplication.common.data.database

import androidx.room.TypeConverter
import java.math.BigDecimal
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

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String {
        return value.toString()
    }

    @TypeConverter
    fun toBigDecimal(value: String): BigDecimal {
        return value.toBigDecimal()
    }
}
