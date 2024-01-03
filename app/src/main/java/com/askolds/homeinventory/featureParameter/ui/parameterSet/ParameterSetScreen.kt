package com.askolds.homeinventory.featureParameter.ui.parameterSet

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import com.askolds.homeinventory.featureParameter.ui.NavigationParameters
import com.askolds.homeinventory.featureParameter.ui.listItem.ParameterListItemRow
import com.askolds.homeinventory.ui.DarkLightPreviews
import com.askolds.homeinventory.ui.PreviewScaffold
import com.askolds.homeinventory.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.ui.rememberCanNavigate
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun ParameterSetScreen(
    viewModel: ParameterSetViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val canNavigate = rememberCanNavigate()
    ParameterSetContent(
        state = viewModel.state,
        navigateToEditParameterSet = {
            if (canNavigate)
                navController.navigate(
                    route = NavigationParameters.ParameterSetEdit.getRoute(viewModel.state.parameterSet.id)
                )
        },
        navigateBack = {
            if (canNavigate)
                navController.navigateUp()
        },
        navigateToParameter = {parameterId ->
            if (canNavigate)
                navController.navigate(route = NavigationParameters.ParameterView.getRoute(parameterId))
        },
        appBarsObject = appBarsObject,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ParameterSetContent(
    state: ParameterSetState,
    navigateToEditParameterSet: () -> Unit,
    navigateBack: () -> Unit,
    navigateToParameter: (parameterId: Int) -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {

    Box(Modifier.fillMaxSize()) {
        ParameterSetParameterList(
            state.parameters,
            navigateToParameter,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier
        )
        ParameterSetTopAppBar(
            appBarsObject = appBarsObject,
            title = state.parameterSet.name,
            navigateBack = navigateBack,
            navigateToEditParameterSet = navigateToEditParameterSet
        )
    }
}

@Composable
private fun ParameterSetTopAppBar(
    appBarsObject: AppBarsObject,
    title: String,
    navigateBack: () -> Unit,
    navigateToEditParameterSet: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(title)
        },
        navigationIcon = {
            IconButton(
                onClick = {
                    navigateBack()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.ArrowBack,
                    "Navigate back",
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navigateToEditParameterSet()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    "Edit parameter set",
                )
            }
        },
        appBarsObject = appBarsObject
    )
}

@Composable
private fun ParameterSetParameterList(
    parameterList: List<ParameterListItem>,
    navigateToParameter: (parameterId: Int) -> Unit,
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
            ParameterListItemRow(
                text = parameter.name,
                isSelected = false,
                modifier = Modifier.clickable {
                    navigateToParameter(parameter.id)
                }
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
private fun ParameterSetListContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map {
            ParameterListItem(id = it, name = "Item $it", selected = false)
        }
        val parameterSet = ParameterSet(id = 0, name = "Parameter set")
        val state = ParameterSetState(parameterSet = parameterSet, parameters = items)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ParameterSetContent(
                state = state,
                navigateToEditParameterSet = { },
                navigateBack = { },
                navigateToParameter = { },
                appBarsObject = appBarsObject
            )
        }
    }
}