package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.BottomAppBarDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun BoxScope.ImageNavOverlay(
    appBarsObject: AppBarsObject,
    closeOverlay: () -> Unit
) {
    var hideBottomBar by remember { mutableStateOf(true) }
    if (hideBottomBar) {
        appBarsObject.appBarsState.HideBottomBar(true)
    } else {
        appBarsObject.appBarsState.ShowBottomBar(false)
    }


    BackHandler(
        enabled = true
    ) {
        hideBottomBar = false
        closeOverlay()
    }



    val zModifier = Modifier.zIndex(9999f) // turn it up to 11!
    Box(
        Modifier
            .zIndex(10f)
            .fillMaxSize()

            .background(Color.Black.copy(alpha = 0.5f))
            .pointerInput(Unit) { }
    )
    AsyncImage(
        model = null,
        placeholder = if (LocalInspectionMode.current) {
            painterResource(id = R.drawable.test_image)
        } else {
            null
        },
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = zModifier.align(Alignment.Center)
    )
    BottomAppBar(
        actions = {
            IconButton(onClick = { /* do something */ }) {
                Icon(Icons.Filled.Check, contentDescription = "Localized description")
            }
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* do something */ },
                containerColor = BottomAppBarDefaults.bottomAppBarFabColor,
                elevation = FloatingActionButtonDefaults.bottomAppBarFabElevation()
            ) {
                Icon(Icons.Filled.Check, "Localized description")
            }
        },
        modifier = zModifier.align(Alignment.BottomCenter)
    )
}

@Preview
@Composable
fun ImageNavOverlayPreview() {
    HomeInventoryTheme {
        Box(
            Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            ImageNavOverlay(
                getPreviewAppBarsObject(),
                {}
            )
        }
    }
}