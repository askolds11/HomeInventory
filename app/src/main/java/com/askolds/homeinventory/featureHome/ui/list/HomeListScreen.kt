package com.askolds.homeinventory.featureHome.ui.list

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.House
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.CreateFAB
import com.askolds.homeinventory.ui.navigation.composables.DeleteFAB
import com.askolds.homeinventory.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeListScreen(
    viewModel: HomeListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    // prevent double navigation
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentState = lifecycleOwner.lifecycle.currentState
    val canNavigate = remember(currentState) {
        currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    val isAnySelected by remember {
        derivedStateOf {
            viewModel.state.selectedCount > 0
        }
    }

    // "Nested scaffold"
    Box(Modifier.fillMaxSize()) {
        // TODO: If need more than 2 events, make interface to clean up code
        HomeListContent(
            homeList = viewModel.state.homeList,
            navigateToHome = { homeId ->
                navController.navigate(route = NavigationHome.Home.getRoute(homeId))
            },
            selectHome = { id: Int, selected: Boolean, index: Int ->
                viewModel.selectItem(id, selected, index)
            },
            unselectAll = viewModel::unselectAll,
            canNavigate = canNavigate,
            isAnySelected = isAnySelected,
            contentPadding = PaddingValues(
                top = appBarsObject.appBarsState.topPadding,
                bottom = appBarsObject.appBarsState.bottomPadding,
            ),
            modifier = modifier.fillMaxSize(),
        )
        if (isAnySelected) {
            TopAppBar(
                title = { Text("Selected items: ${viewModel.state.selectedCount}") },
                navigationIcon = {
                    IconButton(
                        onClick = { viewModel.unselectAll() },
                        Modifier
                            .padding(horizontal = 12.dp)
                    ) {
                        Icon(
                            Icons.Filled.ArrowBack,
                            "Unselect items",
                        )
                    }
                },
                appBarsObject = appBarsObject
            )
        } else {
            SearchBar(
                query = viewModel.state.query,
                onQueryChange = { viewModel.search(it) },
                clearQuery = { viewModel.search("") },
                appBarsObject = appBarsObject
            )
        }
        if (isAnySelected) {
            DeleteFAB(
                appBarsObject.appBarsState,
                contentDescription = "Delete homes",
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                viewModel.deleteSelected()
            }
        } else {
            CreateFAB(
                appBarsObject.appBarsState,
                contentDescription = "Add home",
                modifier = Modifier
                    .padding(end = 16.dp, bottom = 16.dp)
                    .align(Alignment.BottomEnd)
            ) {
                if (canNavigate)
                    navController.navigate(route = NavigationHome.Create.route)
            }
        }

    }

}

/**
 * @param homeList Home list
 * @param navigateToHome Navigate to a home
 * @param selectHome (Un)select a home
 * @param unselectAll Unselect all homes
 * @param canNavigate Is navigation enabled
 * @param isAnySelected Whether any home in the list is selected
 * @param contentPadding Content padding
 * @param modifier Modifier
 */
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeListContent(
    homeList: List<HomeListItem>,
    navigateToHome: (id: Int) -> Unit,
    selectHome: (Int, Boolean, Int) -> Unit,
    unselectAll: () -> Unit,
    canNavigate: Boolean,
    isAnySelected: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    BackHandler(
        enabled = isAnySelected
    ) {
        unselectAll()
    }
    //NavBarVisibility(lazyListState, 1000)

    val lazyListState = rememberLazyListState()
    //val bottomBarNestedScrollConnection = rememberBottomBarNestedScrollConnection(lazyListState)


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        //.nestedScroll(bottomBarNestedScrollConnection),
        contentPadding = PaddingValues(
            //start = WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current),
            //end = WindowInsets.safeContent.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current),
            bottom = contentPadding.calculateBottomPadding(),
            top = contentPadding.calculateTopPadding()
        ),
        state = lazyListState
    ) {
        itemsIndexed(items = homeList, key = { _, home -> home.id }) { index, home ->
            val clickModifier = Modifier.combinedClickable(
                interactionSource = remember { MutableInteractionSource() },
                // Don't show click when selecting
                indication = if (!isAnySelected) LocalIndication.current else null,
                onLongClick = { selectHome(home.id, !home.selected, index) },
                // If any item is selected, select/unselect the sequent ones, otherwise navigate
                onClick = {
                    if (isAnySelected) {
                        selectHome(home.id, !home.selected, index)
                    } else if (canNavigate) {
                        navigateToHome(home.id)
                    }
                },
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
    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
    val selectedModifier =
        if (isSelected)
            Modifier.background(selectedColor)
        else
            Modifier

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
                //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
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
fun HomeListContentPreview() {
    val items = (1..5).map { HomeListItem(it, "Item $it") }
    HomeInventoryTheme {
        HomeListContent(
            items,
            {},
            { _, _, _ -> },
            { },
            canNavigate = true,
            isAnySelected = false,
            PaddingValues(vertical = 60.dp)
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