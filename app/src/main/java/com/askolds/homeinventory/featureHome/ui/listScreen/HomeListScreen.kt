package com.askolds.homeinventory.featureHome.ui.listScreen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.House
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
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
import com.askolds.homeinventory.core.ui.getSelectableClickModifier
import com.askolds.homeinventory.core.ui.getSelectableColorModifier
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.ui.NavigationHome

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeListScreen(
    viewModel: HomeListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    viewModel.getHomeListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    val canNavigate = rememberCanNavigate()

    HomeListContent(
        state = viewModel.state,
        event = viewModel::onEvent,
        appBarsObject = appBarsObject,
        navigateToHome = { homeId ->
            if(canNavigate.value)
                navController.navigate(route = NavigationHome.Home.getRoute(homeId))
        },
        navigateToCreateHome = {
            if(canNavigate.value)
                navController.navigate(route = NavigationHome.Create.route)
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
fun HomeListContent(
    state: HomeListState,
    event: (HomeListEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateToHome: (id: Int) -> Unit,
    navigateToCreateHome: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val isAnySelected by remember(state.selectedCount.value) {
        derivedStateOf {
            state.selectedCount.value > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        HomeList(
            homeList = state.homeList,
            isAnySelected = isAnySelected,
            selectItem = { id, selected, index -> event(HomeListEvent.SelectItem(id, selected, index)) },
            unselectAll = { event(HomeListEvent.UnselectAll) },
            navigateToHome = navigateToHome,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier
        )
        SearchSelectAppBars(
            appBarsObject = appBarsObject,
            selectedCount = state.selectedCount.value,
            isAnySelected = isAnySelected,
            query = state.query.value,
            unselectAll = { event(HomeListEvent.UnselectAll)},
            searchQueryChanged = { event(HomeListEvent.QueryChanged(it)) },
            placeholderText = stringResource(R.string.search_homes_placeholder)
        )
        SelectCreateDeleteFAB(
            appBarsObject = appBarsObject,
            isAnySelected = isAnySelected,
            deleteFABContentDescription = stringResource(R.string.delete_homes),
            createFABContentDescription = stringResource(R.string.create_home),
            deleteOnClick = { event(HomeListEvent.DeleteSelected) },
            createOnClick = { navigateToCreateHome() }
        )
    }
}

@Composable
private fun HomeList(
    homeList: List<HomeListItem>,
    isAnySelected: Boolean,
    selectItem: (Int, Boolean, Int) -> Unit,
    unselectAll: () -> Unit,
    navigateToHome: (id: Int) -> Unit,
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
    ) {
        itemsIndexed(items = homeList, key = { _, home -> home.id }) { index, home ->
            val clickModifier = Modifier.getSelectableClickModifier(
                isAnySelected = isAnySelected,
                onLongClick = { selectItem(home.id, !home.selected, index) },
                onClickWhenSelected = { selectItem(home.id, !home.selected, index) },
                onClickWhenNotSelected = { navigateToHome(home.id) },
            )

            HomeListItemRow(
                imageUri = home.imageUri,
                text = home.name,
                isSelected = home.selected,
                modifier = clickModifier
            )
            if (index < homeList.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@Composable
fun HomeListItemRow(
    imageUri: String?,
    text: String,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    val selectedModifier = Modifier.getSelectableColorModifier(isSelected)

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .then(selectedModifier)
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(44.dp),
    ) {
        val imageModifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f)
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                RoundedCornerShape(8.dp)
            )
        if (imageUri == null) {
            Image(
                imageVector = Icons.Outlined.House,
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
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeListItemRowPreview() {
    HomeInventoryTheme(darkTheme = true, dynamicColor = false) {
        HomeListItemRow(imageUri = null, text = "Tests123", isSelected = false)
    }
}

@DarkLightPreviews
@Composable
private fun HomeListContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map { HomeListItem(it, "Home $it") }
        val state = HomeListState(items.toMutableStateList())
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            HomeListContent(
                state = state,
                event = { },
                appBarsObject = appBarsObject,
                navigateToHome = { },
                navigateToCreateHome = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun HomeListContentPreviewSelected() {
    HomeInventoryTheme {
        val items1 = (1..3).map { HomeListItem(it, "Home $it") }
        val items2 = (4..5).map { HomeListItem(it, "Home $it", selected = true) }
        val items = items1 + items2
        val selectedCount = remember { mutableIntStateOf(2) }
        val state = HomeListState(items.toMutableStateList(), selectedCount = selectedCount)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            HomeListContent(
                state = state,
                event = { },
                appBarsObject = appBarsObject,
                navigateToHome = { },
                navigateToCreateHome = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}