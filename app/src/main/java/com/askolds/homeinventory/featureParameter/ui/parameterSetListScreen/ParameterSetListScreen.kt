package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.List
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.ui.NavigationParameters
import com.askolds.homeinventory.featureParameter.ui.listItem.ParameterSetListItemRow
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.SearchSelectAppBars
import com.askolds.homeinventory.core.ui.SelectCreateDeleteFAB
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.getSelectableClickModifier
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun ParameterSetListScreen(
    viewModel: ParameterSetListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    val canNavigate = rememberCanNavigate()

    ParameterSetListContent(
        state = viewModel.state,
        event = viewModel::onEvent,
        navigateToParameterSet = { parameterSetId ->
            if (canNavigate.value)
                navController.navigate(
                    route = NavigationParameters.ParameterSetView.getRoute(parameterSetId)
                )
        },
        navigateToParameterListScreen = {
            if (canNavigate.value)
                navController.navigate(route = NavigationParameters.ParameterList.route )
        },
        navigateToCreateParameterSet = {
            if (canNavigate.value)
                navController.navigate(route = NavigationParameters.ParameterSetCreate.route)
        },
        appBarsObject = appBarsObject,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun ParameterSetListContent(
    state: ParameterSetListState,
    event: (ParameterSetListEvent) -> Unit,
    navigateToParameterSet: (parameterSetId: Int) -> Unit,
    navigateToCreateParameterSet: () -> Unit,
    navigateToParameterListScreen: () -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    // https://medium.com/@theAndroidDeveloper/yet-another-pitfall-in-jetpack-compose-you-must-be-aware-of-225a1d07d033
    val isAnySelected by remember(state.selectedCount) {
        derivedStateOf {
            state.selectedCount > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        ParameterSetListItems(
            parameterSetList = state.parameterSetList,
            navigateToParameterSet = { parameterSetId ->
                navigateToParameterSet(parameterSetId)
            },
            selectParameterSet = { id, selected, boolean ->
                event(ParameterSetListEvent.SelectItem(id, selected, boolean))
            },
            unselectAll = { event(ParameterSetListEvent.UnselectAll) },
            navigateToParameterListScreen = navigateToParameterListScreen,
            isAnySelected = isAnySelected,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier.fillMaxSize(),
        )
        SearchSelectAppBars(
            appBarsObject = appBarsObject,
            selectedCount = state.selectedCount,
            isAnySelected = isAnySelected,
            query = state.query,
            unselectAll = { event(ParameterSetListEvent.UnselectAll) },
            searchQueryChanged = { newText: String ->
                event(ParameterSetListEvent.QueryChanged(newText))
            },
            placeholderText = stringResource(R.string.search_parameter_sets_placeholder)
        )
        SelectCreateDeleteFAB(
            appBarsObject = appBarsObject,
            isAnySelected = isAnySelected,
            deleteFABContentDescription = stringResource(R.string.delete_parameter_sets),
            createFABContentDescription = stringResource(R.string.create_parameter_set),
            deleteOnClick = { event(ParameterSetListEvent.DeleteSelected) },
            createOnClick = navigateToCreateParameterSet
        )
    }
}

@Composable
private fun ParameterSetListItems(
    parameterSetList: List<ParameterSetListItem>,
    navigateToParameterSet: (parameterSetId: Int) -> Unit,
    selectParameterSet: (Int, Boolean, Int) -> Unit,
    unselectAll: () -> Unit,
    navigateToParameterListScreen: () -> Unit,
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
        contentPadding = PaddingValues(
            bottom = contentPadding.calculateBottomPadding(),
            top = contentPadding.calculateTopPadding()
        ),
    )  {
        item {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clickable {
                        navigateToParameterListScreen()
                    }
                    .padding(horizontal = 12.dp, vertical = 8.dp)
                    .fillMaxWidth()
                    .height(44.dp),
            ) {
                Image(
                    imageVector = Icons.Outlined.List,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = Modifier
                        .fillMaxHeight()
                        .aspectRatio(1f)
                        .padding(8.dp)
                )

                Text(
                    stringResource(R.string.all_parameters),
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )
            }
        }

        item {
            Divider(color = MaterialTheme.colorScheme.primary)
        }

        itemsIndexed(
            items = parameterSetList,
            key = { _, parameterSet -> parameterSet.id }
        ) { index, parameterSet ->
            val clickModifier = Modifier.getSelectableClickModifier(
                isAnySelected = isAnySelected,
                onLongClick = { selectParameterSet(parameterSet.id, !parameterSet.selected, index) },
                onClickWhenSelected = { selectParameterSet(parameterSet.id, !parameterSet.selected, index) },
                onClickWhenNotSelected = {
                    navigateToParameterSet(parameterSet.id)
                },
            )
            ParameterSetListItemRow(
                text = parameterSet.name,
                isSelected = parameterSet.selected,
                modifier = clickModifier
            )
            if (index < parameterSetList.lastIndex) {
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }
        }
    }
}

@DarkLightPreviews
@Composable
fun ParameterSetListContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map { ParameterSetListItem(it, "Item $it") }
        val state = ParameterSetListState(items.toMutableStateList())

        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ParameterSetListContent(
                state = state,
                event = { },
                navigateToParameterListScreen = { },
                navigateToParameterSet = { },
                navigateToCreateParameterSet = { },
                appBarsObject = appBarsObject
            )
        }
    }
}