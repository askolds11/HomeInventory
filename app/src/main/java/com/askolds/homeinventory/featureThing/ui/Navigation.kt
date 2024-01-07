package com.askolds.homeinventory.featureThing.ui

import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.askolds.homeinventory.featureThing.ui.formScreen.ThingFormScreen
import com.askolds.homeinventory.featureThing.ui.formScreen.ThingFormViewModel
import com.askolds.homeinventory.featureThing.ui.formScreen.parameterSets.ThingParameterSetsScreen
import com.askolds.homeinventory.featureThing.ui.formScreen.parameters.ThingParametersScreen
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.featureThing.ui.thingScreen.ThingScreen
import com.askolds.homeinventory.featureThing.ui.thingScreen.ThingViewModel
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.NavigationGraph
import com.askolds.homeinventory.core.ui.navigation.defaultEnterTransition
import com.askolds.homeinventory.core.ui.navigation.defaultExitTransition
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayViewModel

sealed class NavigationHomeThing(val route: String, val args: String? = null) {
    data object Create : NavigationHomeThing(route = "${NavigationGraph.Home.route}/thing/create", args = "/{homeId}?parentId={parentId}") {
        fun getRoute(homeId: Int, parentId: Int? = null): String {
            val parentString = if (parentId != null) "?parentId=$parentId" else ""
            return "${route}/$homeId$parentString"
        }

    }
    data object Edit : NavigationHomeThing(route = "${NavigationGraph.Home.route}/thing/edit", args = "/{thingId}") {
        fun getRoute(thingId: Int) = "${this.route}/$thingId"
    }
    data object FormParameters : NavigationHomeThing(route = "${NavigationGraph.Home.route}/thing/formParameters")
    data object FormParameterSets : NavigationHomeThing(route = "${NavigationGraph.Home.route}/thing/formParameterSets")
    data object Thing : NavigationHomeThing(route = "${NavigationGraph.Home.route}/thing", args = "/{homeId}/{thingId}") {
        fun getRoute(homeId: Int, thingId: Int) = "${this.route}/$homeId/$thingId"
    }
}

fun NavGraphBuilder.homeThingGraph(
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val appBarsState = appBarsObject.appBarsState
    val formRoute = "navigation-thing-form"
    navigation(startDestination = with (NavigationHomeThing.Create) {route + args}, route = formRoute) {
        composable(
            route = with (NavigationHomeThing.Create) {route + args},
            arguments = listOf(
                navArgument("homeId") {type = NavType.IntType},
                navArgument("parentId") {nullable = true;/* type = NavType.IntType*/}
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(formRoute)
            }
            val formViewModel = hiltViewModel<ThingFormViewModel>(parentEntry)

            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ThingFormScreen(formViewModel, navController, appBarsObject)
        }
        composable(
            route = with (NavigationHomeThing.Edit) {route + args},
            arguments = listOf(
                navArgument("thingId") {type = NavType.IntType},
            ),
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(formRoute)
            }
            val formViewModel = hiltViewModel<ThingFormViewModel>(parentEntry)

            appBarsState.ShowAppBars(lockTop = true, lockBottom = false)
            ThingFormScreen(formViewModel, navController, appBarsObject)
        }
        composable(
            route = NavigationHomeThing.FormParameters.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(formRoute)
            }
            val formViewModel = hiltViewModel<ThingFormViewModel>(parentEntry)

            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            ThingParametersScreen(formViewModel, navController, appBarsObject)
        }
        composable(
            route = NavigationHomeThing.FormParameterSets.route,
            enterTransition = { defaultEnterTransition(this) },
            exitTransition = { defaultExitTransition(this) }
        ) {backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(formRoute)
            }
            val formViewModel = hiltViewModel<ThingFormViewModel>(parentEntry)

            appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
            ThingParameterSetsScreen(formViewModel, navController, appBarsObject)
        }
    }

    composable(
        route = with (NavigationHomeThing.Thing) {route + args},
        arguments = listOf(navArgument("homeId") { type = NavType.IntType }, navArgument("thingId") { type = NavType.IntType }),
        enterTransition = { defaultEnterTransition(this) },
        exitTransition = { defaultExitTransition(this) }
    ) {
        appBarsState.ShowAppBars(lockTop = false, lockBottom = false)
        ThingScreen(
            hiltViewModel<ThingViewModel>(),
            hiltViewModel<ThingListViewModel>(),
            hiltViewModel<ImageNavOverlayViewModel>(),
            navController,
            appBarsObject
        )
    }
}