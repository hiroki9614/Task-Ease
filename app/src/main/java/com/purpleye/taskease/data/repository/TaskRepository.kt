package com.purpleye.taskease.data.repository

import com.purpleye.taskease.data.database.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    fun getAllTaskStream(): Flow<List<Task>>

    fun getTaskStream(id: Int): Flow<Task>

    suspend fun insertTask(task: Task)

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun allDeleteTask()

}