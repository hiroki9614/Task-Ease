package com.purpleye.taskease.di

import android.content.Context
import androidx.room.Room
import com.purpleye.taskease.data.database.TaskEaseDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TaskEaseModule {

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, TaskEaseDatabase::class.java, "task_ease_database").build()

    @Singleton
    @Provides
    fun provideTaskEaseDao(db: TaskEaseDatabase) = db.taskDao()
}