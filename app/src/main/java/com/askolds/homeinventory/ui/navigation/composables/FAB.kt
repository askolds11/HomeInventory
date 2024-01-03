package com.askolds.homeinventory.ui.navigation.composables

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
}

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
fun CollapsingFAB(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = FloatingActionButtonDefaults.shape,
    containerColor: Color = FloatingActionButtonDefaults.containerColor,
    contentColor: Color = contentColorFor(containerColor),
    elevation: FloatingActionButtonElevation = FloatingActionButtonDefaults.elevation(),
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    appBarsState: AppBarsState,
    paddingValues: PaddingValues = PaddingValues(),
    content: @Composable () -> Unit,
) {
    DisposableEffect(Unit) {
        onDispose {
            appBarsState.ClearBottomFABPadding()
        }
    }
    var fabHeightAndSpace by remember {
        mutableStateOf(0.dp)
    }
    appBarsState.SetBottomFABPadding(
        padding = fabHeightAndSpace + with (paddingValues) {
            calculateBottomPadding() + calculateTopPadding()
        }
    )

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

    val localDensity = LocalDensity.current
    val keyboardPadding = WindowInsets.ime.getBottom(localDensity)
    val fabKeyboardOffsetFloat = remember(
        keyboardPadding,
        appBarsState.bottomPaddingInt,
        appBarsState.bottomBarState.heightOffset
    ) {
        derivedStateOf {
            // if keyboard above bottom bar, follow it
            if (appBarsState.bottomPaddingInt + appBarsState.bottomBarState.heightOffset < keyboardPadding) {
                return@derivedStateOf keyboardPadding.toFloat() - appBarsState.bottomPaddingPx
                // if keyboard below bottom bar state, do nothing
            } else {
                return@derivedStateOf 0f
            }
        }
    }
    val fabKeyboardOffsetDp = with(LocalDensity.current) { fabKeyboardOffsetFloat.value.toDp() }

    val offsetModifier = Modifier
        .offset(y = fabOffsetDp - fabKeyboardOffsetDp)

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(paddingValues)
            .then(offsetModifier)
            .onGloballyPositioned { coords ->
                fabHeightAndSpace = with(localDensity) { coords.size.height.toDp() }
            },
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content,
    )
}