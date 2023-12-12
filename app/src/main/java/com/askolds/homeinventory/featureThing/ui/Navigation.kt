package com.askolds.homeinventory.featureThing.ui

import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.askolds.homeinventory.featureThing.ui.form.ThingFormScreen
import com.askolds.homeinventory.featureThing.ui.form.ThingFormViewModel
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.featureThing.ui.thing.ThingScreen
import com.askolds.homeinventory.featureThing.ui.thing.ThingViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.NavigationGraph
import com.askolds.homeinventory.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.ui.navigation.defaultExitTransition

sealed class NavigationThing(val route: String, val args: String? = null) {
    data object Create : NavigationThing(route = "${NavigationGraph.Thing.route}/create", args = "/{homeId}?parentId={parentId}") {
        fun getRoute(homeId: Int, parentId: Int? = null): String {
            val parentString = if (parentId != null) "?parentId=$parentId" else ""
            return "${route}/$homeId$parentString"
        }

    }
    data object Edit : NavigationThing(route = "${NavigationGraph.Thing.route}/edit", args = "/{thingId}") {
        fun getRoute(thingId: Int) = "${this.route}/$thingId"
    }
    data object Thing : NavigationThing(route = NavigationGraph.Thing.route, args = "/{homeId}/{thingId}") {
        fun getRoute(homeId: Int, thingId: Int) = "${this.route}/$homeId/$thingId"
    }
}

fun NavGraphBuilder.thingGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    val appBarsState = appBarsObject.appBarsState
    navigation(startDestination = with (NavigationThing.Thing) {route + args}, route = NavigationGraph.Thing.route) {
        composable(
            route = with (NavigationThing.Create) {route + args},
            arguments = listOf(
                navArgument("homeId") {type = NavType.IntType},
                navArgument("parentId") {nullable = true;/* type = NavType.IntType*/}
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            ThingFormScreen(hiltViewModel<ThingFormViewModel>(), navController)
        }
        composable(
            route = with (NavigationThing.Edit) {route + args},
            arguments = listOf(
                navArgument("thingId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = true, lockBottom = true)
            ThingFormScreen(hiltViewModel<ThingFormViewModel>(), navController)
        }
        composable(
            route = with (NavigationThing.Thing) {route + args},
            arguments = listOf(navArgument("homeId") { type = NavType.IntType }, navArgument("thingId") { type = NavType.IntType }),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {
            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            ThingScreen(
                hiltViewModel<ThingViewModel>(),
                hiltViewModel<ThingListViewModel>(),
                navController,
                appBarsObject
            )
        }
    }
}