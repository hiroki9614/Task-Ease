package com.purpleye.taskease.data.database

import androidx.room.TypeConverter
import com.purpleye.taskease.util.DateFormatUtil
import java.time.LocalDate

/**
 * Roomへのデータ保存、取得時に使用する型変換用クラス
 */
class DateConverter {
    @TypeConverter
    fun fromString(value: String): LocalDate {
        return DateFormatUtil.stringToDate(value)
    }

    @TypeConverter
    fun localDateToString(date: LocalDate): String {
        return DateFormatUtil.dateToStrong(date)
    }
}

