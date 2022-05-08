package ru.internetcloud.workorderapplication.domain.common

import java.text.SimpleDateFormat
import java.util.Date

class DateConverter {
    companion object {

        private const val DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"
        private const val DATE_FORMAT = "dd.MM.yyyy"
        private const val TIME_FORMAT = "HH : mm"

        fun fromStringToDate(dateString: String): Date {
            return SimpleDateFormat(DATE_TIME_FORMAT).parse(dateString)
        }

        fun getDateString(date: Date?): String {
            val sdf = SimpleDateFormat(DATE_FORMAT)
            return date?.let { sdf.format(date) } ?: ""
        }

        fun getTimeString(date: Date?): String {
            val sdf = SimpleDateFormat(TIME_FORMAT)
            return date?.let { sdf.format(date) } ?: ""
        }
    }
}
