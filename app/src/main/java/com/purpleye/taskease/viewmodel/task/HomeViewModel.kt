package com.purpleye.taskease.viewmodel.task

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.purpleye.taskease.data.database.Task
import com.purpleye.taskease.data.repository.TaskDataRepository
import com.purpleye.taskease.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val taskRepository: TaskDataRepository
): ViewModel() {

    val uiState: StateFlow<HomeUiState> =
        taskRepository.getAllTaskStream().map {HomeUiState(it)}
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = HomeUiState()
            )

    suspend fun deleteTask(task: Task) {
        taskRepository.deleteTask(task)
    }

}

data class HomeUiState(
    val taskList: List<Task> = listOf()
)