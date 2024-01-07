package com.askolds.homeinventory.featureHome.ui.homeScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
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
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.featureThing.ui.NavigationHomeThing
import com.askolds.homeinventory.featureThing.ui.list.ThingListEvent
import com.askolds.homeinventory.featureThing.ui.list.ThingListState
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.featureThing.ui.list.thingItems

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    listViewModel: ThingListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    viewModel.getHomeFlow.collectAsStateWithLifecycle(initialValue = Unit)
    listViewModel.getThingListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    val canNavigate = rememberCanNavigate()

    HomeContent(
        homeState = viewModel.state,
        thingListState = listViewModel.state,
        thingListEvent = listViewModel::onEvent,
        navigateToEditHome = {
            if (canNavigate.value)
                navController.navigate(route = NavigationHome.Edit.getRoute(viewModel.state.home.value.id))
        },
        navigateToThing = { homeId, thingId ->
            if (canNavigate.value)
                navController.navigate(route = NavigationHomeThing.Thing.getRoute(homeId, thingId))
        },
        navigateToCreateThing = { homeId, thingId ->
            if (canNavigate.value)
                navController.navigate(route = NavigationHomeThing.Create.getRoute(homeId, thingId))
        },
        appBarsObject = appBarsObject,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun HomeContent(
    homeState: HomeState,
    thingListState: ThingListState,
    thingListEvent: (ThingListEvent) -> Unit,
    navigateToEditHome: () -> Unit,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    navigateToCreateThing: (homeId: Int, thingId: Int?) -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    val isAnySelected by remember(thingListState.selectedCount.value) {
        derivedStateOf {
            thingListState.selectedCount.value > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        HomeListItems(
            home = homeState.home.value,
            thingList = thingListState.thingList,
            navigateToThing = navigateToThing,
            selectThing = { id: Int, selected: Boolean, index: Int ->
                thingListEvent(ThingListEvent.SelectItem(id, selected, index))
            },
            unselectAll = { thingListEvent(ThingListEvent.DeleteSelected) },
            isAnySelected = isAnySelected,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier,
        )
        SearchSelectAppBars(
            appBarsObject = appBarsObject,
            selectedCount = thingListState.selectedCount.value,
            isAnySelected = isAnySelected,
            query = thingListState.query.value,
            unselectAll = { thingListEvent(ThingListEvent.UnselectAll) },
            searchQueryChanged = { thingListEvent(ThingListEvent.QueryChanged(it) )},
            trailingIcon = {
                IconButton(
                    onClick = navigateToEditHome,
                    Modifier
                        .padding(horizontal = 12.dp)
                ) {
                    Icon(
                        Icons.Filled.Edit,
                        stringResource(R.string.edit_home),
                    )
                }
            },
            placeholderText = stringResource(R.string.search_in_home_placeholder)
        )
        SelectCreateDeleteFAB(
            appBarsObject = appBarsObject,
            isAnySelected = isAnySelected,
            deleteFABContentDescription = stringResource(R.string.delete_things),
            createFABContentDescription = stringResource(R.string.create_thing),
            deleteOnClick = { thingListEvent(ThingListEvent.DeleteSelected) },
            createOnClick = {
                navigateToCreateThing(homeState.home.value.id, null)
            }
        )
    }
}

@Composable
private fun HomeListItems(
    home: Home,
    thingList: List<ThingListItem>,
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

    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    )  {
        item {
            HomeHeader(imageUri = home.image?.imageUri, text = home.name)
        }

        item {
            Divider(color = MaterialTheme.colorScheme.outline)
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
private fun HomeHeader(
    imageUri: String?,
    text: String,
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
        if (imageUri == null) {
            Image(
                imageVector = Icons.Outlined.Home,
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

@DarkLightPreviews
@Composable
private fun ThingContentPreview() {
    HomeInventoryTheme {
        val home = Home(name = "My Thing")
        val homeStatee = remember { mutableStateOf(home) }

        val homeState = HomeState(
            home = homeStatee,
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
            HomeContent(
                homeState = homeState,
                thingListState = thingListState,
                thingListEvent = {},
                appBarsObject = appBarsObject,
                navigateToThing = {_, _ -> },
                navigateToCreateThing = {_, _ -> },
                navigateToEditHome = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}