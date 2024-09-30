@file:OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3Api::class)

package com.purpleye.taskease.ui.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.purpleye.taskease.R
import com.purpleye.taskease.TaskEaseTopAppBar
import com.purpleye.taskease.data.repository.TaskDataRepository
import com.purpleye.taskease.di.TaskEaseModule
import com.purpleye.taskease.ui.components.CustomTextField
import com.purpleye.taskease.ui.components.DatePickerDialog
import com.purpleye.taskease.ui.components.StarPriority
import com.purpleye.taskease.ui.navigation.NavigationDestination
import com.purpleye.taskease.ui.theme.TaskEaseTheme
import com.purpleye.taskease.util.DateFormatUtil
import com.purpleye.taskease.viewmodel.task.TaskDetails
import com.purpleye.taskease.viewmodel.task.TaskEditUiState
import com.purpleye.taskease.viewmodel.task.TaskEditViewModel
import kotlinx.coroutines.launch

object TaskEditDestination : NavigationDestination {
    override val route = "edit"
    override val titleRes = R.string.task_edit_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEditScreen(
    id: Int = 0,
    navigateBack: () -> Unit,
    onNavigateUp: () -> Unit,
    canNavigateBack: Boolean = true,
    viewModel: TaskEditViewModel = hiltViewModel(),
) {
    val coroutineScope = rememberCoroutineScope()

    if (id > 0) {
        LaunchedEffect(id) {
            viewModel.loadTask(id)
        }
    }

    Scaffold(
        topBar = {
            TaskEaseTopAppBar(
                title = stringResource(id = R.string.task_edit_title),
                canNavigateBack = canNavigateBack,
                navigateUp = onNavigateUp
            )
        }
    ) { innerPadding ->
        TaskEditBody(
            onTaskValueChange = viewModel::updateTaskDetails,
            modifier = Modifier
                .padding(
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    top = innerPadding.calculateTopPadding(),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current),
                )
                .fillMaxSize(),
            onSaveClick = {
                coroutineScope.launch {
                    viewModel.saveTask()
                    navigateBack()
                }
            },
            uiState = viewModel.uiState
        )
    }
}

@Composable
private fun TaskEditBody(
    onTaskValueChange: (TaskDetails) -> Unit,
    modifier: Modifier = Modifier,
    onSaveClick: () -> Unit,
    uiState: TaskEditUiState
) {

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(dimensionResource(id = R.dimen.padding_medium)),
    ) {
        // タスク入力フォーム
        TaskInputForm(
            taskDetails = uiState.taskDetails,
            onValueChange = onTaskValueChange,
            modifier = Modifier.fillMaxWidth()
        )

        // スペーサーにより保存ボタンを最下部に配置する
        Spacer(modifier = Modifier.weight(1f))

        // 保存ボタン
        SaveButton(
            onSaveClick = {
                onSaveClick()
            },
            uiState = uiState,
            modifier = Modifier.align(Alignment.End)
        )
    }
}

@Composable
private fun TaskInputForm(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit
) {

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.padding_small))
    ) {
        // Task Name
        CustomTextField(
            value = taskDetails.name,
            onValueChange = { onValueChange(taskDetails.copy(name = it)) },
            modifier = Modifier
                .padding(vertical = dimensionResource(id = R.dimen.padding_small))
                .fillMaxWidth(),
            placeholder = { Text(stringResource(R.string.task_name)) }
        )

        // Priority
        PrioritySelection(
            taskDetails = taskDetails,
            onValueChange = onValueChange
        )

        // Dead Line
        DatePickerButton(
            taskDetails = taskDetails,
            onValueChange = onValueChange
        )
    }
}

@Composable
private fun PrioritySelection(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.task_priority))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                StarPriority(
                    priority = taskDetails.priority,
                    onValueChange = {onValueChange(taskDetails.copy(priority = it))},
                    modifier = Modifier
                        .align(Alignment.CenterEnd)
                )
            }
        }
    }
}

@Composable
private fun DatePickerButton(
    taskDetails: TaskDetails,
    modifier: Modifier = Modifier,
    onValueChange: (TaskDetails) -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }

    Column(
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = modifier
                .padding(dimensionResource(id = R.dimen.padding_small))
                .fillMaxWidth()
        ) {
            Text(text = stringResource(R.string.task_dead_line))

            Column(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = DateFormatUtil.dateToStrong(taskDetails.deadLine),
                    modifier = Modifier.padding(end = dimensionResource(id = R.dimen.padding_small), bottom = dimensionResource(id = R.dimen.padding_small))
                )

                Button(
                    onClick = { showDialog = true },
                ) {
                    Text(text = stringResource(R.string.dead_line_select))
                }
            }
        }
    }
    // 日付選択ダイアログ
    DatePickerDialog(
        showDialog = showDialog,
        selectionDate = taskDetails.deadLine,
        onDismissRequest = { showDialog = false },
        onDateSelected = { date ->
            onValueChange(taskDetails.copy(deadLine = DateFormatUtil.stringToDate(date)))
        }
    )
}

@Composable
private fun SaveButton(
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier,
    uiState: TaskEditUiState
) {
    Button(
        onClick = onSaveClick,
        enabled = uiState.isEntryValid,
        shape = MaterialTheme.shapes.small,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(text = stringResource(R.string.save_action))
    }
}

@Composable
@Preview
private fun TaskEditScreenPreview() {
    TaskEaseTheme {
        TaskEditScreen(
            navigateBack = { },
            onNavigateUp = { },
            viewModel = TaskEditViewModel(
                TaskDataRepository(
                    TaskEaseModule.provideTaskEaseDao(
                        TaskEaseModule.provideDatabase(LocalContext.current)
                    )
                )
            )
        )
    }
}