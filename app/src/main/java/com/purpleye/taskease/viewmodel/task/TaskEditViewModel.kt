package com.purpleye.taskease.viewmodel.task

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.purpleye.taskease.data.database.Task
import com.purpleye.taskease.data.repository.TaskDataRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TaskEditViewModel @Inject constructor(
    private val taskRepository: TaskDataRepository
): ViewModel() {

    var uiState by mutableStateOf(TaskEditUiState())
        private set

    fun updateTaskDetails(taskDetails: TaskDetails) {
        uiState = TaskEditUiState(
            taskDetails = taskDetails,
            isEntryValid = validateInput(taskDetails)
        )
    }

    suspend fun loadTask(id: Int) {
        taskRepository.getTaskStream(id).collect {
            uiState = TaskEditUiState(
                taskDetails = TaskDetails(
                    id = it.id,
                    name = it.task,
                    priority = it.priority,
                    deadLine = it.deadLine
                )
            )
        }
    }

    suspend fun saveTask() {
        if (validateInput()) {
            taskRepository.insertTask(uiState.taskDetails.toTask())
            delay(100)
        }
    }

    private fun validateInput(taskDetails: TaskDetails = uiState.taskDetails): Boolean {
        return with(taskDetails) {
            name.isNotBlank()
        }
    }
}

data class TaskEditUiState(
    val taskDetails: TaskDetails = TaskDetails(),
    val isEntryValid: Boolean = false
)

data class TaskDetails(
    val id: Int = 0,
    val name: String = "",
    val priority: Int = 3,
    val deadLine: LocalDate = LocalDate.now(),
)

fun TaskDetails.toTask(): Task = Task(
    id = id,
    task = name,
    priority = priority,
    deadLine = deadLine
)