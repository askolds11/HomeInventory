package com.askolds.homeinventory.featureThing.ui.formScreen.parameters

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
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.ui.listItem.SelectableParameterListItemRow
import com.askolds.homeinventory.featureThing.ui.formScreen.ThingFormViewModel
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun ThingParametersScreen(
    viewModel: ThingFormViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    viewModel.parametersListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    ThingParametersContent(
        state = viewModel.parametersState,
        event = viewModel::parametersOnEvent,
        appBarsObject = appBarsObject,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ThingParametersContent(
    state: ThingParametersState,
    event: (ThingParametersEvent) -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {

    Box(Modifier.fillMaxSize()) {
        ThingParametersParameterList(
            parameterList = state.parameterList,
            selectParameter = { parameterId, selected, index ->
                event(ThingParametersEvent.SelectItem(parameterId, selected, index))
            },
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier
        )
        SearchBar(
            query = state.query,
            onQueryChange = { event(ThingParametersEvent.QueryChanged(it)) },
            clearQuery = { event(ThingParametersEvent.QueryChanged("")) },
            appBarsObject = appBarsObject
        )
    }
}

@Composable
private fun ThingParametersParameterList(
    parameterList: List<ParameterListItem>,
    selectParameter: (parameterId: Int, selected: Boolean, index: Int) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {

        itemsIndexed(
            items = parameterList, key = { _, parameter -> parameter.id }
        ) { index, parameter ->
            val clickModifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                // Don't show click when selecting
                indication = null,
                onClick = {
                    selectParameter(parameter.id, !parameter.selected, index)
                },
            )

            SelectableParameterListItemRow(
                text = parameter.name,
                isSelected = parameter.selected,
                modifier = clickModifier
            )
            if (index < parameterList.lastIndex) {
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
private fun ThingParametersContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map {
            ParameterListItem(id = it, name = "Item $it", selected = false)
        }.toMutableStateList()
        val state = ThingParametersState(parameterList = items)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ThingParametersContent(
                state = state,
                event = { },
                appBarsObject = appBarsObject
            )
        }
    }
}