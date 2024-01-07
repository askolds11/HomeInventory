package com.askolds.homeinventory.core.ui.fab

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsState

@Composable
fun DeleteFAB(
    appBarsState: AppBarsState,
    modifier: Modifier = Modifier,
    contentDescription: String,
    onClick: () -> Unit,
) {
    CollapsingFAB(
        onClick = onClick,
        appBarsState = appBarsState,
        containerColor = MaterialTheme.colorScheme.errorContainer,
        modifier = modifier,
    ) {
        Icon(
            Icons.Filled.Delete,
            contentDescription
        )
    }
}