package com.askolds.homeinventory.ui.home

import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.askolds.homeinventory.ui.navigation.NavigationBase

sealed class NavigationHome(val route: String) {
    data object List : NavigationHome(route = "${NavigationBase.Home.route}/home")
}

fun NavGraphBuilder.homeGraph(
    navController: NavController,
    //lazyListState: LazyListState
) {
    navigation(startDestination = NavigationHome.List.route, route = NavigationBase.Home.route) {
        composable(route = NavigationHome.List.route) {
            HomeListScreen(viewModel(), navController/*, lazyListState*/)
        }
    }
}