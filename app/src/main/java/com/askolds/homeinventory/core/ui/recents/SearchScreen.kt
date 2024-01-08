package com.askolds.homeinventory.core.ui.recents

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.askolds.homeinventory.R

@Composable
fun SearchScreen(
    navController: NavController,
) {
    SearchContent()
}

@Composable
fun SearchContent(
    modifier: Modifier = Modifier
) {
    Text(
        text = stringResource(R.string.search)
    )
}