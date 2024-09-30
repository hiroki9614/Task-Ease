package com.purpleye.taskease.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "task")
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Int,
    val task: String,
    val priority: Int,
    val deadLine: LocalDate
)
