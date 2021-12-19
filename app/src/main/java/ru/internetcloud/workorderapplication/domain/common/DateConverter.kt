package ru.internetcloud.workorderapplication.domain.common

import java.text.SimpleDateFormat
import java.util.Date

class DateConverter {
    companion object {
        fun fromStringToDate(dateString: String): Date {
            return SimpleDateFormat("yyyy/MM/dd HH:mm:ss").parse(dateString)
        }
    }
}