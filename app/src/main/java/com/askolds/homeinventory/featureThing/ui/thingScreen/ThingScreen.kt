package com.askolds.homeinventory.featureThing.ui.thingScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.SearchSelectAppBars
import com.askolds.homeinventory.core.ui.SelectCreateDeleteFAB
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlay
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayEdit
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayEvent
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayState
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.ImageNavOverlayViewModel
import com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay.things.ImageNavOverlayThings
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.featureThing.ui.NavigationHomeThing
import com.askolds.homeinventory.featureThing.ui.list.ThingListEvent
import com.askolds.homeinventory.featureThing.ui.list.ThingListState
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.featureThing.ui.list.thingItems
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

@Composable
fun ThingScreen(
    viewModel: ThingViewModel,
    listViewModel: ThingListViewModel,
    imageNavOverlayViewModel: ImageNavOverlayViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    viewModel.getThingFlow.collectAsStateWithLifecycle(initialValue = Unit)
    viewModel.getThingParametersFlow.collectAsStateWithLifecycle(initialValue = Unit)
    viewModel.getThingParameterSetsFlow.collectAsStateWithLifecycle(initialValue = Unit)

    listViewModel.getThingListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    val canNavigate = rememberCanNavigate()

    ThingContent(
        thingState = viewModel.state,
        thingListState = listViewModel.state,
        thingListEvent = listViewModel::onEvent,
        imageNavOverlayState = imageNavOverlayViewModel.state,
        imageNavOverlayEvent = imageNavOverlayViewModel::onEvent,
        imageNavOverlayFlow = imageNavOverlayViewModel.getListFlow,
        imageNavOverlayThingsFlow = imageNavOverlayViewModel.getThingsListFlow,
        navigateToThing = { homeId, thingId ->
            if (canNavigate.value)
                navController.navigate(route = NavigationHomeThing.Thing.getRoute(homeId, thingId))
        },
        navigateToCreateThing = { homeId, thingId ->
            if (canNavigate.value)
                navController.navigate(route = NavigationHomeThing.Create.getRoute(homeId, thingId))
        },
        navigateToEditThing = {
            if (canNavigate.value)
                navController.navigate(route = NavigationHomeThing.Edit.getRoute(viewModel.state.thing.value.id))
        },
        appBarsObject = appBarsObject,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun ThingContent(
    thingState: ThingState,
    thingListState: ThingListState,
    thingListEvent: (ThingListEvent) -> Unit,
    imageNavOverlayState: ImageNavOverlayState,
    imageNavOverlayEvent: (ImageNavOverlayEvent) -> Unit,
    imageNavOverlayFlow: SharedFlow<Unit>,
    imageNavOverlayThingsFlow: SharedFlow<Unit>,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    navigateToCreateThing: (homeId: Int, thingId: Int?) -> Unit,
    navigateToEditThing: () -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    val isAnySelected by remember(thingListState.selectedCount) {
        derivedStateOf {
            thingListState.selectedCount.value > 0
        }
    }

    var imageNavOverlay by remember { mutableStateOf(false) }
    var imageNavOverlayEdit by remember { mutableStateOf(false) }
    var imageNavOverlayThings by remember { mutableStateOf(false) }
    
    Box(Modifier.fillMaxSize()) {
        if (!imageNavOverlayThings) {
            ThingListItems(
                thingState = thingState,
                thingList = thingListState.thingList,
                imageOnClick = { if (thingState.thing.value.imageId != null) imageNavOverlay = !imageNavOverlay },
                navigateToThing = navigateToThing,
                selectThing = { id: Int, selected: Boolean, index: Int ->
                    thingListEvent(ThingListEvent.SelectItem(id, selected, index))
                },
                unselectAll = { thingListEvent(ThingListEvent.DeleteSelected) },
                isAnySelected = isAnySelected,
                contentPadding = appBarsObject.appBarsState.getContentPadding(),
                modifier = modifier,
            )
        }
        SearchSelectAppBars(
            appBarsObject = appBarsObject,
            selectedCount = thingListState.selectedCount.value,
            isAnySelected = isAnySelected,
            query = thingListState.query.value,
            unselectAll = { thingListEvent(ThingListEvent.UnselectAll) },
            searchQueryChanged = { thingListEvent(ThingListEvent.QueryChanged(it)) },
            trailingIcon = {
                IconButton(
                    onClick = navigateToEditThing,
                    Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(R.string.edit_thing),
                    )
                }
            },
            placeholderText = stringResource(R.string.search_in_thing_placeholder)
        )
        SelectCreateDeleteFAB(
            appBarsObject = appBarsObject,
            isAnySelected = isAnySelected,
            deleteFABContentDescription = stringResource(R.string.delete_things),
            createFABContentDescription = stringResource(R.string.create_thing),
            deleteOnClick = { thingListEvent(ThingListEvent.DeleteSelected) },
            createOnClick = {
                with(thingListState) {
                    navigateToCreateThing(homeId.value, thingId.value!!)
                }
            }
        )
        if (imageNavOverlay) {
            ImageNavOverlay(
                imageNavOverlayState,
                imageNavOverlayEvent,
                imageNavOverlayFlow,
                imageUri = thingState.thing.value.imageUri!!,
                appBarsObject = appBarsObject,
                navigateToThing,
                closeOverlay = {
                    imageNavOverlayEdit = false
                    imageNavOverlay = false
                    imageNavOverlayThings = false
                },
                openEditOverlay = {
                    imageNavOverlayEdit = true
                    imageNavOverlay = false
                    imageNavOverlayThings = false
                }
            )
        } else if (imageNavOverlayEdit) {
            ImageNavOverlayEdit(
                imageNavOverlayState,
                imageNavOverlayEvent,
                imageNavOverlayFlow,
                imageUri = thingState.thing.value.imageUri!!,
                appBarsObject = appBarsObject,
                closeEditOverlay = {
                    imageNavOverlayEdit = false
                    imageNavOverlay = true
                    imageNavOverlayThings = false
                },
                openThingsOverlay = {
                    imageNavOverlayEdit = false
                    imageNavOverlay = false
                    imageNavOverlayThings = true
                }
            )
        } else if (imageNavOverlayThings) {
            ImageNavOverlayThings(
                imageNavOverlayState,
                imageNavOverlayEvent,
                imageNavOverlayThingsFlow,
                appBarsObject = appBarsObject,
                closeThingsOverlay = {
                    imageNavOverlayEdit = true
                    imageNavOverlay = false
                    imageNavOverlayThings = false
                }
            )
        } else {
            LaunchedEffect(Unit) {
                appBarsObject.appBarsState.showBottomBar(false)
            }
        }
    }
}

@Composable
private fun ThingListItems(
    thingState: ThingState,
    thingList: List<ThingListItem>,
    imageOnClick: () -> Unit,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    selectThing: (id: Int, selected: Boolean, index: Int) -> Unit,
    unselectAll: () -> Unit,
    isAnySelected: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    BackHandler(
        enabled = isAnySelected
    ) {
        unselectAll()
    }

    val parametersExpanded = remember { mutableStateOf(false) }
    val parameterSetsExpanded = remember { mutableStateOf(false) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        item {
            with(thingState.thing.value) {
                ThingHeader(
                    imageUri = imageUri,
                    isContainer = isContainer,
                    text = name,
                    imageOnClick = imageOnClick
                )
            }
        }

        item {
            CollapsibleHeader(
                stringResource(R.string.parameters),
                parametersExpanded.value
            ) { parametersExpanded.value = !parametersExpanded.value }
        }

        if (parametersExpanded.value) {
            itemsIndexed(
                items = thingState.thingParameters,
                key = { _, thingParameter -> "param-${thingParameter.parameterId}" }
            ) { index, thingParameter ->
                ThingParameterValueItemRow(
                    parameterName = thingParameter.parameterName,
                    value = thingParameter.value ?: "",
                    Modifier.padding(start = 24.dp, end = 16.dp)
                )

                if (index < thingState.thingParameters.lastIndex) {
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 16.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }

            if (thingState.thingParameters.size > 0) {
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

        }

        item {
            CollapsibleHeader(
                stringResource(R.string.parameter_sets),
                parameterSetsExpanded.value
            ) { parameterSetsExpanded.value = !parameterSetsExpanded.value }
        }

        if (parameterSetsExpanded.value) {
            thingState.thingParameterSets.forEachIndexed { _, parameterSet ->
                item {
                    ThingParameterSetHeaderItemRow(parameterSetName = parameterSet.parameterSetName)
                }
                itemsIndexed(
                    items = parameterSet.thingParameters,
                    key = { _, thingSetParameter ->
                        "paramSet-${parameterSet.parameterSetId}-${thingSetParameter.parameterId}"
                    }
                ) { parameterSetParameterIndex, thingSetParameter ->
                    ThingParameterValueItemRow(
                        parameterName = thingSetParameter.parameterName,
                        value = thingSetParameter.value ?: "",
                        Modifier.padding(start = 24.dp, end = 16.dp)
                    )

                    if (parameterSetParameterIndex < thingState.thingParameters.lastIndex) {
                        Divider(
                            color = MaterialTheme.colorScheme.outline,
                            modifier = Modifier
                                .padding(start = 20.dp, end = 16.dp)
                                .padding(vertical = 8.dp)
                        )
                    }
                }
            }
        }

        item {
            Divider(color = MaterialTheme.colorScheme.primary)
        }

        thingItems(
            thingList,
            isAnySelected,
            selectThing,
            navigateToThing,
        )


    }
}


@Composable
private fun ThingHeader(
    imageUri: String?,
    isContainer: Boolean,
    text: String,
    imageOnClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
    ) {
        val imageModifier = Modifier
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                RoundedCornerShape(8.dp)
            )
            .weight(1f)
            .aspectRatio(3f / 4f)
            .clickable { imageOnClick() }

        if (imageUri == null) {
            Image(
                imageVector =
                if (isContainer)
                    ImageVector.vectorResource(R.drawable.package_2_outlined)
                else
                    Icons.Outlined.Hardware,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                modifier = imageModifier.padding(8.dp)
            )
        } else {
            AsyncImage(
                model = imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
                modifier = imageModifier
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(3f)
        )
    }
}

@Composable
private fun CollapsibleHeader(
    text: String,
    expanded: Boolean,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            if (expanded)
                Icon(
                    Icons.Filled.ExpandLess,
                    stringResource(R.string.collapse, text),
                )
            else
                Icon(
                    Icons.Filled.ExpandMore,
                    stringResource(R.string.expand, text),
                )
        }
    }
}

@Composable
private fun ThingParameterValueItemRow(
    parameterName: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Text(parameterName)
        }
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth()) {
            Text(
                text = value,
                modifier = Modifier
                    .padding(start = 12.dp)
                    .fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ThingParameterSetHeaderItemRow(
    parameterSetName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Text(parameterSetName)
        }
        Spacer(Modifier.height(4.dp))
    }
}

@DarkLightPreviews
@Composable
private fun ThingContentPreview() {
    HomeInventoryTheme {
        val thing = Thing(
            name = "My Thing",
            isContainer = false,
            parentId = null,
            homeId = 0,
            imageId = null
        )
        val thingStatee = remember { mutableStateOf(thing) }
        val thingParameters = (1..2).map {
            ThingParameter(
                id = it,
                value = "Value $it",
                parameterId = it,
                parameterSetId = null,
                parameterSetParameterId = null,
                thingParameterSetId = null,
                parameterName = "Parameter $it"
            )
        }
        val thingState = ThingState(
            thing = thingStatee,
            thingParameters = thingParameters.toMutableStateList()
        )

        val thingList = (1..5).map {
            ThingListItem(
                id = it,
                homeId = 0,
                name = "Thing $it",
                isContainer = false,
            )
        }
        val thingListState = ThingListState(
            thingList = thingList.toMutableStateList()
        )

        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ThingContent(
                thingState = thingState,
                thingListState = thingListState,
                thingListEvent = {},
                imageNavOverlayState = ImageNavOverlayState(),
                imageNavOverlayEvent = { },
                imageNavOverlayFlow = MutableSharedFlow(),
                imageNavOverlayThingsFlow = MutableSharedFlow(),
                appBarsObject = appBarsObject,
                navigateToThing = { _, _ -> },
                navigateToCreateThing = { _, _ -> },
                navigateToEditThing = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}