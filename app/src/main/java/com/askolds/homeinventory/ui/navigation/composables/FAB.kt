package com.askolds.homeinventory.ui.navigation.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState

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

@Composable
private fun CollapsingFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    appBarsState: AppBarsState,
    content: @Composable () -> Unit,
) {
    val bottomSafeDrawing = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val fabOffsetFloat = remember {
        derivedStateOf {
            // if bottom bar above navigation bar, follow it
            if (appBarsState.bottomPaddingInt + appBarsState.bottomBarState.heightOffset < bottomSafeDrawing) {
                return@derivedStateOf -bottomSafeDrawing.toFloat()// + (appBarsState.bottomBarState.heightOffsetLimit - appBarsState.bottomBarState.heightOffset)
                // if bottom bar below (behind) navigation bar, stop above the navigation bar
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
        modifier = modifier
            .then(offsetModifier),
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content,
    )
}