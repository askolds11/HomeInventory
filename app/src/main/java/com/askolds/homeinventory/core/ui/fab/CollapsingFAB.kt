package com.askolds.homeinventory.core.ui.fab

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material.ripple.RippleTheme
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
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
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsState
import kotlin.math.roundToInt

object NoRippleTheme : RippleTheme {
    @Composable
    override fun defaultColor() = Color.Unspecified

    @Composable
    override fun rippleAlpha(): RippleAlpha = RippleAlpha(0.0f, 0.0f, 0.0f, 0.0f)
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
    val fabHeightAndSpace = remember { mutableStateOf(0.dp) }
    appBarsState.SetBottomFABPadding(
        padding = fabHeightAndSpace.value + with (paddingValues) {
            calculateBottomPadding() + calculateTopPadding()
        }
    )

    val bottomSafeDrawing = WindowInsets.navigationBars.getBottom(LocalDensity.current)
    val fabOffsetInt = {
        with (appBarsState.bottomBarState) {
            // if bottom bar below (behind) navigation bar, stop above the navigation bar
            if (-heightOffsetLimit.roundToInt() + heightOffset < bottomSafeDrawing) {
                return@with -bottomSafeDrawing
                //if bottom bar above navigation bar, follow it
            } else {
                return@with (heightOffsetLimit - heightOffset).toInt()
            }
        }
    }

    val localDensity = LocalDensity.current
    val keyboardPadding = WindowInsets.ime.getBottom(localDensity)
    val fabKeyboardOffsetInt = {
        with (appBarsState.bottomBarState) {
            // if keyboard above bottom bar, follow it
            if (-heightOffsetLimit.roundToInt() + heightOffset < keyboardPadding) {
                keyboardPadding + fabOffsetInt()
                // if keyboard below bottom bar state, do nothing
            } else {
                0
            }
        }
    }

    val offsetModifier = Modifier
//        .offset(y = fabOffsetDp - fabKeyboardOffsetDp)
        .offset { IntOffset(x = 0, y = fabOffsetInt() - fabKeyboardOffsetInt()) }

    FloatingActionButton(
        onClick = onClick,
        modifier = modifier
            .padding(paddingValues)
            .then(offsetModifier)
            .onGloballyPositioned { coords ->
                fabHeightAndSpace.value = with(localDensity) { coords.size.height.toDp() }
            },
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        elevation = elevation,
        interactionSource = interactionSource,
        content = content,
    )
}