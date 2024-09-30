package com.purpleye.taskease.data.repository

import com.purpleye.taskease.data.database.Task
import com.purpleye.taskease.data.database.TaskDao
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskDataRepository @Inject constructor(private val taskDao: TaskDao): TaskRepository {

    override fun getAllTaskStream(): Flow<List<Task>> {
        return taskDao.getAllTasks()
    }

    override fun getTaskStream(id: Int): Flow<Task> {
        return taskDao.getItem(id)
    }

    override suspend fun insertTask(task: Task) {
        taskDao.insert(task)
    }

    override suspend fun updateTask(task: Task) {
        taskDao.update(task)
    }

    override suspend fun deleteTask(task: Task) {
        taskDao.delete(task)
    }

    override suspend fun allDeleteTask() {
        taskDao.deleteAll()
    }

}