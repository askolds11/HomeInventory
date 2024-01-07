package com.askolds.homeinventory.featureParameter.ui.parameterListScreen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.ui.NavigationParameters
import com.askolds.homeinventory.featureParameter.ui.listItem.ParameterListItemRow
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.SearchSelectAppBars
import com.askolds.homeinventory.core.ui.SelectCreateDeleteFAB
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.getSelectableClickModifier
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

// viewmodels not supported in preview, wrapper
@Composable
fun ParameterListScreen(
    viewModel: ParameterListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
ParameterListContent(
    state = viewModel.state,
    event = viewModel::onEvent,
    appBarsObject = appBarsObject,
    navigateToParameter = { parameterId ->
        navController.navigate(route = NavigationParameters.ParameterView.getRoute(parameterId))
    },
    navigateToCreateParameter = {
        navController.navigate(route = NavigationParameters.ParameterCreate.route )
    },
    modifier = Modifier.fillMaxSize())
}

@Composable
private fun ParameterListContent(
    state: ParameterListState,
    event: (ParameterListEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateToParameter: (parameterId: Int) -> Unit,
    navigateToCreateParameter: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // https://medium.com/@theAndroidDeveloper/yet-another-pitfall-in-jetpack-compose-you-must-be-aware-of-225a1d07d033
    val isAnySelected by remember(state.selectedCount) {
        derivedStateOf {
            state.selectedCount > 0
        }
    }

    Box(Modifier.fillMaxSize()) {
        ParameterListItems(
            parameterList = state.parameterList,
            navigateToParameter = navigateToParameter,
            selectParameter = { id, selected, boolean ->
                event(ParameterListEvent.SelectItem(id, selected, boolean))
            },
            unselectAll = { event(ParameterListEvent.UnselectAll) },
            isAnySelected = isAnySelected,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier.fillMaxSize(),
        )

        SearchSelectAppBars(
            appBarsObject = appBarsObject,
            selectedCount = state.selectedCount,
            isAnySelected = isAnySelected,
            query = state.query,
            unselectAll = { event(ParameterListEvent.UnselectAll) },
            searchQueryChanged = { newText: String ->
                event(ParameterListEvent.QueryChanged(newText))
            },
            placeholderText = stringResource(R.string.search_parameters_placeholder)
        )

        SelectCreateDeleteFAB(
            appBarsObject = appBarsObject,
            isAnySelected = isAnySelected,
            deleteFABContentDescription = stringResource(R.string.delete_parameters),
            createFABContentDescription = stringResource(R.string.create_parameter),
            deleteOnClick = { event(ParameterListEvent.DeleteSelected) },
            createOnClick = navigateToCreateParameter
        )
    }

}

@Composable
private fun ParameterListItems(
    parameterList: List<ParameterListItem>,
    navigateToParameter: (parameterId: Int) -> Unit,
    selectParameter: (Int, Boolean, Int) -> Unit,
    unselectAll: () -> Unit,
    isAnySelected: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    val canNavigate = rememberCanNavigate()
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
        itemsIndexed(
            items = parameterList, key = { _, parameter -> parameter.id }
        ) { index, parameter ->
            val clickModifier = Modifier.getSelectableClickModifier(
                isAnySelected = isAnySelected,
                onLongClick = { selectParameter(parameter.id, !parameter.selected, index) },
                onClickWhenSelected = { selectParameter(parameter.id, !parameter.selected, index) },
                onClickWhenNotSelected = {
                    if (canNavigate.value)
                        navigateToParameter(parameter.id)
                }
            )

            ParameterListItemRow(
                text = parameter.name,
                isSelected = parameter.selected,
                modifier = clickModifier
            )
            if (index < parameterList.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.outline)
            }
        }
    }
}

@DarkLightPreviews
@Composable
fun ParameterListContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map { ParameterListItem(it, "Item $it") }
        val state = ParameterListState(items.toMutableStateList())
        val appbarsObject = getPreviewAppBarsObject()
        PreviewScaffold(
            appBarsObject =appbarsObject
        ) {
            ParameterListContent(
                state = state,
                event = { },
                appBarsObject = appbarsObject,
                { },
                { }
            )
        }

    }
}