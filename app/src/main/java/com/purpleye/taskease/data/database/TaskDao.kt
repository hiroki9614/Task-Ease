package com.purpleye.taskease.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: Task)

    @Update
    suspend fun update(task: Task)

    @Delete
    suspend fun delete(task: Task)

    @Query("SELECT * from task WHERE id = :id")
    fun getItem(id: Int): Flow<Task>

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flow<List<Task>>

    @Query("DELETE FROM task")
    suspend fun deleteAll()
}