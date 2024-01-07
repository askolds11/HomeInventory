package com.askolds.homeinventory.featureParameter.ui.parameterFormScreen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.PreviewScaffold
import com.askolds.homeinventory.core.ui.fab.SaveFAB
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.validation.ValidateName

@Composable
fun ParameterFormScreen(
    viewModel: ParameterFormViewModel,
    appBarsObject: AppBarsObject,
    navController: NavController,
) {
    val canNavigate = rememberCanNavigate()
    ParameterFormContent(
        state = viewModel.state,
        event = { viewModel.onEvent(it) },
        appBarsObject = appBarsObject,
        navigateBack = {
            if (canNavigate.value)
                navController.navigateUp()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ParameterFormContent(
    state: ParameterFormState,
    event: (ParameterFormEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {

    Box(Modifier.fillMaxSize()) {
        ParameterForm(
            state = state,
            event = event,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier
        )
        ParameterFormTopAppBar(
            appBarsObject = appBarsObject,
            isEditMode = state.isEditMode,
            navigateBack = { navigateBack() })
        val localFocusManager = LocalFocusManager.current
        SaveFAB(
            appBarsObject = appBarsObject,
            saveStatus = state.saveStatus,
            enabled = true,
            navigateBack = navigateBack,
            onClick = {
                event(ParameterFormEvent.Submit); localFocusManager.clearFocus()
            }
        )
    }
}

@Composable
private fun ParameterFormTopAppBar(
    appBarsObject: AppBarsObject,
    isEditMode: Boolean,
    navigateBack: () -> Unit,
) {
    TopAppBar(
        title = {
            Text(
                stringResource(
                    if (isEditMode)
                        R.string.edit_parameter
                    else
                        R.string.create_parameter
                )
            )
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
        appBarsObject = appBarsObject
    )
}

@Composable
private fun ParameterForm(
    state: ParameterFormState,
    event: (ParameterFormEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        val paddingModifier = Modifier.padding(horizontal = 16.dp)
        item(key = "nameField") {
            OutlinedTextField(
                value = state.parameter.name,
                onValueChange = { event(ParameterFormEvent.NameChanged(it)) },
                label = { Text(stringResource(R.string.parameter_name)) },
                isError = state.nameValidation != null,
                supportingText = {
                    val text = when (state.nameValidation) {
                        ValidateName.ERROR.NULL_OR_BLANK -> stringResource(
                            R.string.validation_required, stringResource(R.string.parameter_name)
                        )

                        ValidateName.ERROR.ALREADY_EXISTS -> stringResource(
                            R.string.validation_alreadyexists
                        )

                        null -> null
                    }
                    if (text != null) {
                        Text(text)
                    }
                },
                singleLine = true,
                modifier = paddingModifier.fillMaxWidth()
            )
        }
    }
}

@DarkLightPreviews
@Composable
private fun ParameterSetListContentPreview() {
    HomeInventoryTheme {
        val parameter = Parameter(id = 0, name = "Parameter name")
        val state = ParameterFormState(parameter = parameter)
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ParameterFormContent(
                state = state,
                event = {},
                appBarsObject = appBarsObject,
                navigateBack = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}