package com.askolds.homeinventory.core.ui.navigation.composables

import android.content.res.Configuration
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.CustomNavigationBar
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun BottomBar(
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    BottomBarContent(
        navController,
        appBarsObject,
        modifier
    )
}

@Composable
fun BottomBarContent(
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    val items = remember {
        listOf(
            NavigationBase.Home,
            NavigationBase.Recents,
            NavigationBase.Parameters
        )
    }

    CustomNavigationBar(
        tonalElevation = 0.dp,
        appBarsObject = appBarsObject,
        modifier = modifier
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

@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4_XL
)
@Preview(
    name = "Light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4_XL
)
@Composable
fun BottomBarPreview() {
    HomeInventoryTheme {
        BottomBar(navController = rememberNavController(), appBarsObject = getPreviewAppBarsObject())
    }
}