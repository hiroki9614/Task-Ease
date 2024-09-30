package com.purpleye.taskease.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.purpleye.taskease.ui.home.HomeDestination
import com.purpleye.taskease.ui.home.HomeScreen
import com.purpleye.taskease.ui.task.TaskEditDestination
import com.purpleye.taskease.ui.task.TaskEditScreen

@Composable
fun TaskEaseNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController, startDestination = HomeDestination.route, modifier = modifier
    ) {
        composable(route = HomeDestination.route) {
            HomeScreen(
                navigateToTaskEdit = { id ->
                    navController.navigate(TaskEditDestination.route + "/$id")
                }
            )
        }

        composable(
            route = TaskEditDestination.route + "/{id}",
            arguments = listOf(navArgument("id") {type = NavType.IntType})
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            TaskEditScreen(
                id = id,
                navigateBack = { navController.popBackStack() },
                onNavigateUp = { navController.navigateUp() }
            )
        }

    }
}