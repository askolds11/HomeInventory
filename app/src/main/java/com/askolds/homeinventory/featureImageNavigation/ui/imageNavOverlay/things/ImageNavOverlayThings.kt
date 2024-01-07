package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.things

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayEvent
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayState
import com.askolds.homeinventory.featureThing.ui.list.thingItems
import kotlinx.coroutines.flow.SharedFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BoxScope.ImageNavOverlayThings(
    state: ImageNavOverlayState,
    event: (ImageNavOverlayEvent) -> Unit,
    thingsFlow: SharedFlow<Unit>,
    appBarsObject: AppBarsObject,
    closeThingsOverlay: () -> Unit
) {
    thingsFlow.collectAsStateWithLifecycle(initialValue = Unit)

    appBarsObject.appBarsState.HideBottomBar(true)

    BackHandler(
        enabled = true
    ) {
        closeThingsOverlay()
    }


    Box(
        Modifier
            .zIndex(1f)
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            Modifier
                .zIndex(5f)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Top
        ) {
            item {
                TopAppBar(
                    title = { },
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                closeThingsOverlay()
                            },
                            modifier = Modifier
                                .padding(horizontal = 12.dp)
                        ) {
                            Icon(
                                Icons.Filled.ArrowBack,
                                stringResource(R.string.navigate_back),
                            )
                        }
                    },
                    modifier = Modifier.zIndex(5f)
                )
            }

            thingItems(
                state.thingList,
                false,
                {_, _, _ ->
                },
                navigateToThing = { _, thingId ->
                    event(ImageNavOverlayEvent.SelectThing(thingId))
                    closeThingsOverlay()
                },
                modifier = Modifier
                    .zIndex(5f)
            )
        }
    }
}