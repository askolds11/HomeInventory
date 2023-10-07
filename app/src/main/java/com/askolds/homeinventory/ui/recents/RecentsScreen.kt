package com.askolds.homeinventory.ui.recents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.askolds.homeinventory.R

@Composable
fun RecentsScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    RecentsContent(
        modifier = modifier
    )
}

@Composable
fun RecentsContent(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.app_name) + "Recents"
    )
}