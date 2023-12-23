package com.askolds.homeinventory.featureParameter.ui.parameterSetList

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.ui.navigation.appbars.AppBarState
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState
import com.askolds.homeinventory.ui.navigation.composables.CreateFAB
import com.askolds.homeinventory.ui.navigation.composables.DeleteFAB
import com.askolds.homeinventory.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun ParameterSetList(
    state: ParameterSetListState,
    event: (ParameterSetListEvent) -> Unit,
//    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
//    navigateToCreateThing: (homeId: Int, thingId: Int?) -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
    searchBarTrailingIcon: @Composable (() -> Unit)? = null,
    preContent: @Composable (() -> Unit)? = null,
) {
    // prevent double navigation
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentState = lifecycleOwner.lifecycle.currentState
    val canNavigate = remember(currentState) {
        currentState.isAtLeast(Lifecycle.State.RESUMED)
    }

    // https://medium.com/@theAndroidDeveloper/yet-another-pitfall-in-jetpack-compose-you-must-be-aware-of-225a1d07d033
    val isAnySelected by remember(state.selectedCount) {
        derivedStateOf {
            state.selectedCount > 0
        }
    }

//    Box(Modifier.fillMaxSize()) {
//        ThingListContent(
//            thingList = state.thingList,
//            contentPadding = PaddingValues(
//                top = appBarsObject.appBarsState.topPadding,
//                bottom = appBarsObject.appBarsState.bottomPadding,
//            ),
//            navigateToThing = { homeId, thingId ->
//                navigateToThing(homeId, thingId)
//            },
//            selectThing = { id, selected, boolean ->
//                event(ParameterSetListEvent.SelectItem(id, selected, boolean))
//            },
//            unselectAll = { event(ParameterSetListEvent.UnselectAll) },
//            canNavigate = canNavigate,
//            isAnySelected = isAnySelected,
//            modifier = modifier.fillMaxSize(),
//            preContent = preContent
//        )
//        if (isAnySelected) {
//            TopAppBar(
//                title = { Text("Selected items: ${state.selectedCount}") },
//                navigationIcon = {
//                    IconButton(
//                        onClick = { event(ParameterSetListEvent.UnselectAll) },
//                        Modifier
//                            .padding(horizontal = 12.dp)
//                    ) {
//                        Icon(
//                            Icons.Filled.ArrowBack,
//                            "Unselect items",
//                        )
//                    }
//                },
//                appBarsObject = appBarsObject
//            )
//        } else {
//            SearchBar(
//                query = state.query,
//                onQueryChange = { event(ParameterSetListEvent.QueryChanged(it)) },
//                clearQuery = { event(ParameterSetListEvent.QueryChanged("")) },
//                appBarsObject = appBarsObject,
//                trailingIcon = searchBarTrailingIcon
//            )
//        }
//        if (isAnySelected) {
//            DeleteFAB(
//                appBarsObject.appBarsState,
//                contentDescription = "Delete things",
//                modifier = Modifier
//                    .padding(end = 16.dp, bottom = 16.dp)
//                    .align(Alignment.BottomEnd),
//                onClick = { event(ParameterSetListEvent.DeleteSelected) }
//            )
//        } else if (state.isContainer) {
//            CreateFAB(
//                appBarsObject.appBarsState,
//                contentDescription = "Add thing",
//                modifier = Modifier
//                    .padding(end = 16.dp, bottom = 16.dp)
//                    .align(Alignment.BottomEnd),
//            ) {
//                if (canNavigate)
//                    with (state) {
//                        navigateToCreateThing(homeId, thingId)
//                    }
//
//            }
//        }
//    }
}

//@Composable
//private fun ThingListContent(
//    thingList: List<ThingListItem>,
//    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
//    selectThing: (Int, Boolean, Int) -> Unit,
//    unselectAll: () -> Unit,
//    canNavigate: Boolean,
//    isAnySelected: Boolean,
//    contentPadding: PaddingValues,
//    modifier: Modifier = Modifier,
//    preContent: @Composable (() -> Unit)? = null
//) {
//    ThingListItems(
//        thingList,
//        navigateToThing,
//        selectThing,
//        unselectAll,
//        canNavigate,
//        isAnySelected,
//        contentPadding,
//        modifier,
//        preContent,
//    )
//}
//
//@OptIn(ExperimentalFoundationApi::class)
//@Composable
//private fun ThingListItems(
//    thingList: List<ThingListItem>,
//    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
//    selectThing: (Int, Boolean, Int) -> Unit,
//    unselectAll: () -> Unit,
//    canNavigate: Boolean,
//    isAnySelected: Boolean,
//    contentPadding: PaddingValues,
//    modifier: Modifier = Modifier,
//    preContent: @Composable (() -> Unit)?,
//) {
//    BackHandler(
//        enabled = isAnySelected
//    ) {
//        unselectAll()
//    }
//
//    LazyColumn(
//        modifier = modifier
//            .fillMaxSize(),
//        //.nestedScroll(bottomBarNestedScrollConnection),
//        contentPadding = PaddingValues(
//            //start = WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current),
//            //end = WindowInsets.safeContent.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current),
//            bottom = contentPadding.calculateBottomPadding(),
//            top = contentPadding.calculateTopPadding()
//        ),
//    )  {
//
//        item {
//            preContent?.invoke()
//        }
//
//
//        itemsIndexed(items = thingList, key = { _, thing -> thing.id }) { index, thing ->
//            val clickModifier = Modifier.combinedClickable(
//                interactionSource = remember { MutableInteractionSource() },
//                // Don't show click when selecting
//                indication = if (!isAnySelected) LocalIndication.current else null,
//                onLongClick = { selectThing(thing.id, !thing.selected, index) },
//                // If any item is selected, select/unselect the sequent ones, otherwise navigate
//                onClick = {
//                    if (isAnySelected) {
//                        selectThing(thing.id, !thing.selected, index)
//                    } else if (canNavigate) {
//                        navigateToThing(thing.homeId, thing.id)
//                    }
//                },
//            )
//            ThingListItemRow(
//                imageUri = thing.imageUri,
//                text = thing.name,
//                isContainer = thing.isContainer,
//                isSelected = thing.selected,
//                modifier = clickModifier
//            )
//            if (index < thingList.lastIndex) {
//                Divider(color = MaterialTheme.colorScheme.outline)
//            }
//        }
//    }
//}
//
//@Composable
//private fun ThingListItemRow(
//    imageUri: String?,
//    text: String,
//    isContainer: Boolean,
//    isSelected: Boolean,
//    modifier: Modifier = Modifier
//) {
//    val selectedColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
//    val selectedModifier =
//        if (isSelected)
//            Modifier.background(selectedColor)
//        else
//            Modifier
//
//    Row(
//        verticalAlignment = Alignment.CenterVertically,
//        modifier = modifier
//            .then(selectedModifier)
//            .padding(horizontal = 12.dp, vertical = 8.dp)
//            .fillMaxWidth()
//            .height(44.dp),
//    ) {
//        val imageModifier = Modifier
//            .fillMaxHeight()
//            .aspectRatio(1f)
//            .border(
//                BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
//                RoundedCornerShape(8.dp)
//            )
//        if (imageUri == null) {
//            Image(
//                imageVector =
//                if (isContainer)
//                    ImageVector.vectorResource(R.drawable.package_2_outlined)
//                else
//                    Icons.Outlined.Hardware,
//                contentScale = ContentScale.Crop,
//                contentDescription = null,
//                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
//                modifier = imageModifier.padding(8.dp)
//            )
//        } else {
//            AsyncImage(
//                model = imageUri,
//                contentScale = ContentScale.Crop,
//                contentDescription = null,
//                //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
//                modifier = imageModifier
//                    .clip(RoundedCornerShape(8.dp))
//            )
//        }
//        Text(
//            text,
//            overflow = TextOverflow.Ellipsis,
//            modifier = Modifier.padding(horizontal = 8.dp)
//        )
//    }
//}
//
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ParameterSetListPreview() {
    HomeInventoryTheme {
        val items = (1..5).map { ParameterListItem(it, "Item $it") }
        val state = ParameterSetListState(items.toMutableStateList())

        ParameterSetList(
            state = state,
            event = { },
            appBarsObject = AppBarsObject(
                AppBarsState(
                    AppBarState(0f, 0f, 0f),
                    AppBarState(0f, 0f, 0f)
                ),
                AppBarsDefaults.exitAlwaysScrollBehavior()
            )
        )
    }
}