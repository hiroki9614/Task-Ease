package com.purpleye.taskease.ui.home

import SwipeToDeleteLazyColumn
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.purpleye.taskease.R
import com.purpleye.taskease.TaskEaseTopAppBar
import com.purpleye.taskease.data.database.Task
import com.purpleye.taskease.data.repository.TaskDataRepository
import com.purpleye.taskease.di.TaskEaseModule
import com.purpleye.taskease.ui.navigation.NavigationDestination
import com.purpleye.taskease.ui.theme.TaskEaseTheme
import com.purpleye.taskease.viewmodel.task.HomeViewModel
import kotlinx.coroutines.launch
import java.time.LocalDate

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.home_title
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navigateToTaskEdit: (id: Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val homeUiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
             TaskEaseTopAppBar(
                 title = stringResource(id = HomeDestination.titleRes),
                 canNavigateBack = false
             )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    navigateToTaskEdit(-1)
                },
                shape = MaterialTheme.shapes.medium,
                modifier = Modifier.padding(dimensionResource(id = R.dimen.padding_large))
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(R.string.task_edit_title)
                )
            }
        },
    ) { innerPadding ->
        HomeBody(
            taskList = homeUiState.taskList,
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(id = R.dimen.padding_small)),
            contentPadding = innerPadding,
            viewModel = viewModel,
            onTaskClick = navigateToTaskEdit
        )
    }
}

@Composable
private fun HomeBody(
    taskList: List<Task>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    viewModel: HomeViewModel,
    onTaskClick: (Int) -> Unit
) {
    val coroutineScope = rememberCoroutineScope()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        if (taskList.isEmpty()) {
            // タスクが１件もない時はメッセージを表示する
            Text(
                text = stringResource(R.string.no_task_message),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(contentPadding),
            )
        } else {
            SwipeToDeleteLazyColumn(
                items = taskList,
                onClick = {
                    onTaskClick(it.id)
                },
                onItemRemoved = {
                    coroutineScope.launch {
                        viewModel.deleteTask(it)
                    }
                },
                contentPadding = contentPadding
            )
        }
    }
}

@Composable
@Preview
private fun HomeScreenPreview() {
    TaskEaseTheme {
        HomeBody(
            taskList = listOf(
                Task(1, "Task1", 3, LocalDate.now())
            ),
            viewModel = HomeViewModel(
                TaskDataRepository(TaskEaseModule.provideTaskEaseDao(TaskEaseModule.provideDatabase(
                    LocalContext.current)
                ))
            ),
            onTaskClick = {}
        )
    }
}