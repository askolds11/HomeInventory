package com.askolds.homeinventory.featureThing.ui.formScreen.parameterSets

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.ui.listItem.SelectableParameterSetListItemRow
import com.askolds.homeinventory.featureThing.ui.formScreen.ThingFormViewModel
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun ThingParameterSetsScreen(
    viewModel: ThingFormViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    viewModel.parameterSetsListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    ThingParameterSetsContent(
        state = viewModel.parameterSetsState,
        event = viewModel::parameterSetsOnEvent,
        appBarsObject = appBarsObject,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ThingParameterSetsContent(
    state: ThingParameterSetsState,
    event: (ThingParameterSetsEvent) -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {

    Box(Modifier.fillMaxSize()) {
        ThingParameterSetsParameterSetList(
            parameterSetList = state.parameterSetList,
            selectParameterSet = { parameterSetId, selected, index ->
                event(ThingParameterSetsEvent.SelectItem(parameterSetId, selected, index))
            },
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier
        )
        SearchBar(
            query = state.query,
            onQueryChange = { event(ThingParameterSetsEvent.QueryChanged(it)) },
            clearQuery = { event(ThingParameterSetsEvent.QueryChanged("")) },
            appBarsObject = appBarsObject
        )
    }
}

@Composable
private fun ThingParameterSetsParameterSetList(
    parameterSetList: List<ParameterSetListItem>,
    selectParameterSet: (parameterSetId: Int, selected: Boolean, index: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {

        itemsIndexed(
            items = parameterSetList, key = { _, parameterSet -> parameterSet.id }
        ) { index, parameterSet ->
            val clickModifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                // Don't show click when selecting
                indication = null,
                onClick = {
                    selectParameterSet(parameterSet.id, !parameterSet.selected, index)
                },
            )

            SelectableParameterSetListItemRow(
                text = parameterSet.name,
                isSelected = parameterSet.selected,
                modifier = clickModifier
            )
            if (index < parameterSetList.lastIndex) {
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier.padding(start = 16.dp)
                )
            }
        }
    }
}

@DarkLightPreviews
@Composable
private fun ThingParameterSetsContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map {
            ParameterSetListItem(id = it, name = "Item $it", selected = false)
        }.toMutableStateList()
        val state = ThingParameterSetsState(parameterSetList = items)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ThingParameterSetsContent(
                state = state,
                event = { },
                appBarsObject = appBarsObject
            )
        }
    }
}