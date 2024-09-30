package com.purpleye.taskease.util

import java.time.LocalDate
import java.time.format.DateTimeFormatter

class DateFormatUtil {

    companion object {

        private val dateFormat: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd")

        fun stringToDate(string: String): LocalDate {
            return LocalDate.parse(string, dateFormat)
        }

        fun dateToStrong(date: LocalDate): String {
            return date.format(dateFormat)
        }

    }


}