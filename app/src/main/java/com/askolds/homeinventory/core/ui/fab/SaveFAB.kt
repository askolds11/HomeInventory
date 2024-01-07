package com.askolds.homeinventory.core.ui.fab

import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.askolds.homeinventory.core.ui.SaveStatus
import com.askolds.homeinventory.core.ui.getDisabledColor
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.theme.customColors

@Composable
fun BoxScope.SaveFAB(
    appBarsObject: AppBarsObject,
    saveStatus: SaveStatus,
    enabled: Boolean,
    navigateBack: () -> Unit,
    onClick: () -> Unit,
) {
    val isEnabled =
        (saveStatus is SaveStatus.None || saveStatus is SaveStatus.Failed)
                && enabled

    fun disabledColor(color: Color): Color {
        return if (isEnabled)
            color
        else
            color.getDisabledColor()
    }

    CompositionLocalProvider(
        // bugged on API 33
        LocalRippleTheme provides if (isEnabled) LocalRippleTheme.current else NoRippleTheme
    ) {
        CollapsingFAB(
            onClick = {
                if (isEnabled)
                    onClick()
            },
            appBarsState = appBarsObject.appBarsState,
            containerColor = disabledColor(MaterialTheme.customColors.successContainer),
            contentColor = disabledColor(MaterialTheme.customColors.onSuccessContainer),
            modifier = Modifier
                .align(Alignment.BottomEnd),
            paddingValues = PaddingValues(end = 16.dp, bottom = 16.dp)
        ) {
            when (saveStatus) {
                SaveStatus.None, SaveStatus.Failed -> {
                    Icon(
                        Icons.Filled.Done,
                        "Save thing"
                    )
                }

                SaveStatus.Saving -> {
                    CircularProgressIndicator(
                        color = disabledColor(MaterialTheme.customColors.onSuccessContainer),
                    )
                }

                SaveStatus.Saved -> {
                    LaunchedEffect(Unit) {
                        navigateBack()
                    }
                }
            }
        }
    }
}