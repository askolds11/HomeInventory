package com.askolds.homeinventory.ui

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.ui.home.HomeListScreen
import com.askolds.homeinventory.ui.recents.RecentsScreen

sealed class NavigationDestination(val route: String) {
    data object HomeList : NavigationDestination(route = "home/homelist")
    data object Recents : NavigationDestination(route = "recents")
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = "home",
        modifier = modifier
    ) {
        homeGraph(navController)
        composable(route = NavigationDestination.Recents.route) {
            RecentsScreen(navController)
        }
    }

}

// TODO: Move to ui/home etc.?
fun NavGraphBuilder.homeGraph(navController: NavController) {
    navigation(startDestination = NavigationDestination.HomeList.route, route = "home") {
        composable(route = NavigationDestination.HomeList.route) {
            HomeListScreen(navController)
        }
    }
}