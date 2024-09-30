package com.purpleye.taskease.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(DateConverter::class)
@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class TaskEaseDatabase: RoomDatabase() {
    // 使用するDao
    abstract fun taskDao(): TaskDao

}