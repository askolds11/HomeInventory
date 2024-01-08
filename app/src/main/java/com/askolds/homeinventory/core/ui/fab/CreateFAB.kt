package com.askolds.homeinventory.core.ui.fab

import android.content.res.Configuration
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsState
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

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

@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    device = Devices.PIXEL_4_XL
)
@Preview(
    name = "Light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    device = Devices.PIXEL_4_XL
)
@Composable
fun CreateFABPreview() {
    HomeInventoryTheme {
        FloatingActionButton(
            onClick = { },
            content = { Icon(
                Icons.Filled.Delete,
                "contentDescription"
            ) },
        )
    }
}