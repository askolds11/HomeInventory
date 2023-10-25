package com.askolds.homeinventory.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckBox
import androidx.compose.material.icons.filled.House
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.outlined.CheckBoxOutlineBlank
import androidx.compose.material.icons.outlined.House
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.ui.home.homeGraph
import com.askolds.homeinventory.ui.recents.RecentsScreen

sealed class NavigationBase(val route: String, val iconInactive: ImageVector, val iconActive: ImageVector, @StringRes val resourceId: Int) {
    data object Home : NavigationBase(route = "home", Icons.Outlined.House, Icons.Filled.House, R.string.homes)
    data object Recents : NavigationBase(route = "recents", Icons.Outlined.Schedule, Icons.Filled.Schedule, R.string.recents)
    data object Blank : NavigationBase(route = "blank", Icons.Outlined.CheckBoxOutlineBlank, Icons.Filled.CheckBox, R.string.crash)
}

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
) {
    val navBar = remember { NavBarUtils() }
    CompositionLocalProvider(LocalNavBar provides navBar) {
        Scaffold(
            bottomBar = { BottomNavigation(navController) }
        ) { innerPadding ->
            // Sets the bottom padding variable in LocalNavBar to max
            NavBarBottomPadding(paddingValues = innerPadding)

            NavHost(
                navController = navController,
                startDestination = NavigationBase.Home.route,
                modifier = Modifier
                    .fillMaxSize()
            ) {
                homeGraph(navController/*, scrollState*/)
                composable(route = NavigationBase.Recents.route) {
                    RecentsScreen(navController)
                }
            }
        }
    }
}

@Composable
fun BottomNavigation(navController: NavController) {
    val items = listOf(
        NavigationBase.Home,
        NavigationBase.Recents,
        NavigationBase.Blank
    )
    AnimatedVisibility(
        visible = LocalNavBar.current.barVisible,
        enter = slideInVertically(animationSpec = tween(durationMillis = 200), initialOffsetY = { it }),
        exit = slideOutVertically(animationSpec = tween(durationMillis = 200), targetOffsetY = { it })) {

        NavigationBar(
            modifier = Modifier.padding(vertical = 0.dp),
            tonalElevation = 0.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        if (currentDestination?.hierarchy?.any { it.route == screen.route } == true) {
                            Icon(screen.iconActive, contentDescription = null)
                        } else {
                            Icon(screen.iconInactive, contentDescription = null)
                        }
                    },
                    label = {
                        Text(text = stringResource(screen.resourceId))
                    },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            // Pop up to the start destination of the graph to
                            // avoid building up a large stack of destinations
                            // on the back stack as users select items
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            // Avoid multiple copies of the same destination when
                            // reselecting the same item
                            launchSingleTop = true
                            // Restore state when reselecting a previously selected item
                            restoreState = true
                        }
                    }
                )
            }
        }
    }
}