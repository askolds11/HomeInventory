package com.askolds.homeinventory.featureParameter.ui.parameterScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.ui.NavigationParameters
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme

@Composable
fun ParameterScreen(
    viewModel: ParameterViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val canNavigate = rememberCanNavigate()
    ParameterContent(
        state = viewModel.state,
        navigateToEditParameter = {
            if (canNavigate.value)
                navController.navigate(
                    route = NavigationParameters.ParameterEdit.getRoute(viewModel.state.parameter.id)
                )
        },
        navigateBack = {
            if (canNavigate.value)
                navController.navigateUp()
        },
        appBarsObject = appBarsObject,
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ParameterContent(
    state: ParameterState,
    navigateToEditParameter: () -> Unit,
    navigateBack: () -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {

    Box(Modifier.fillMaxSize()) {
        ParameterView(
            state.parameter,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier
        )
        ParameterSetTopAppBar(
            appBarsObject = appBarsObject,
            title = state.parameter.name,
            navigateBack = navigateBack,
            navigateToEditParameter = navigateToEditParameter
        )
    }
}

@Composable
private fun ParameterSetTopAppBar(
    appBarsObject: AppBarsObject,
    title: String,
    navigateBack: () -> Unit,
    navigateToEditParameter: () -> Unit,
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
                    stringResource(R.string.navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    navigateToEditParameter()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    stringResource(R.string.edit_parameter),
                )
            }
        },
        appBarsObject = appBarsObject
    )
}

@Composable
private fun ParameterView(
    parameter: Parameter,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    // Currently nothing. TODO: things/parameter sets containing it?
}

@DarkLightPreviews
@Composable
private fun ParameterSetListContentPreview() {
    HomeInventoryTheme {
        val parameter = Parameter(id = 0, name = "Parameter")
        val state = ParameterState(parameter)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ParameterContent(
                state = state,
                navigateToEditParameter = { },
                navigateBack = { },
                appBarsObject = appBarsObject
            )
        }
    }
}