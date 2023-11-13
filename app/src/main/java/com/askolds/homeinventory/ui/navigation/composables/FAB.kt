package com.askolds.homeinventory.ui.navigation.composables

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState

@Composable
fun CreateFAB(
    appBarsState: AppBarsState,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {
    val bottomSafeDrawing = WindowInsets.safeDrawing.getBottom(LocalDensity.current)
    val fabOffsetFloat = remember {
        derivedStateOf {
            if (appBarsState.bottomPaddingInt + appBarsState.bottomBarState.heightOffset < bottomSafeDrawing) {
                return@derivedStateOf -bottomSafeDrawing.toFloat()// + (appBarsState.bottomBarState.heightOffsetLimit - appBarsState.bottomBarState.heightOffset)
            } else {
                return@derivedStateOf (appBarsState.bottomBarState.heightOffsetLimit - appBarsState.bottomBarState.heightOffset)
            }
        }

    }
    val fabOffsetDp = with (LocalDensity.current) { fabOffsetFloat.value.toDp() }

    val offsetModifier = Modifier
        .offset(y = fabOffsetDp)

    FloatingActionButton(
        onClick = onClick,
        modifier
            .then(offsetModifier)
    ) {
        Icon(
            Icons.Filled.Add,
            "Add home"
        )
    }
}