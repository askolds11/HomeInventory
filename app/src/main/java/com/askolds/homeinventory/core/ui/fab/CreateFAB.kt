package com.askolds.homeinventory.core.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsState

@Composable
fun CreateFAB(
    appBarsState: AppBarsState,
    modifier: Modifier = Modifier,
    contentDescription: String,
    onClick: () -> Unit,
) {
    CollapsingFAB(
        onClick = onClick,
        appBarsState = appBarsState,
        modifier = modifier,
    ) {
        Icon(
            Icons.Filled.Add,
            contentDescription
        )
    }
}