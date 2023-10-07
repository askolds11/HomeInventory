package com.askolds.homeinventory.ui.home

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.ui.NavigationDestination

@Composable
fun HomeListScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeListContent(
        modifier = modifier,
        navController // TODO: Remove
    )
}

@Composable
fun HomeListContent(
    modifier: Modifier = Modifier,
    navController: NavController // TODO: Remove
) {
    Text(
        text = stringResource(R.string.app_name)
    )
    Button(
        onClick = {
            navController.navigate(
                NavigationDestination.Recents.route
            )
        }
    ) {
        Text(
            text = "Button"
        )
    }
}