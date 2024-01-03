package com.askolds.homeinventory.featureParameter.ui.parameterForm

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.validation.ValidateName
import com.askolds.homeinventory.ui.DarkLightPreviews
import com.askolds.homeinventory.ui.PreviewScaffold
import com.askolds.homeinventory.ui.SaveStatus
import com.askolds.homeinventory.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.ui.rememberCanNavigate
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun ParameterFormScreen(
    viewModel: ParameterFormViewModel,
    appBarsObject: AppBarsObject,
    navController: NavController,
) {
    val canNavigate = rememberCanNavigate()
    ParameterFormContent(
        state = viewModel.state,
        event = viewModel::onEvent,
        appBarsObject = appBarsObject,
        navigateBack = {
            if (canNavigate)
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
            appBarsObject = appBarsObject,
            navigateBack = navigateBack,
            modifier = modifier
        )
        ParameterFormTopAppBar(
            appBarsObject = appBarsObject,
            isEditMode = state.isEditMode,
            navigateBack = { navigateBack() })
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
                        R.string.edit_parameter_set
                    else
                        R.string.create_parameter_set
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
                    "Navigate back",
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
    appBarsObject: AppBarsObject,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                appBarsObject.appBarsState.getContentPadding()
            )
            .padding(
                horizontal = 16.dp
            )
    ) {
        OutlinedTextField(
            value = state.parameter.name,
            onValueChange = { event(ParameterFormEvent.NameChanged(it)) },
            label = { Text(stringResource(R.string.home_name)) },
            isError = state.nameValidation != null,
            supportingText = {
                val text = when (state.nameValidation) {
                    ValidateName.ERROR.NULL_OR_BLANK -> stringResource(
                        R.string.validation_required, stringResource(R.string.home_name)
                    )

                    ValidateName.ERROR.ALREADY_EXISTS -> stringResource(
                        R.string.home_validation_alreadyexists
                    )

                    null -> null
                }
                if (text != null) {
                    Text(text)
                }
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        val localFocusManager = LocalFocusManager.current
        Button(
            onClick = {
                event(ParameterFormEvent.Submit); localFocusManager.clearFocus()
            },
            enabled = state.saveEnabled,
            modifier = Modifier.fillMaxWidth()
        ) {
            when (state.saveStatus) {
                SaveStatus.None, SaveStatus.Failed -> {
                    Text(stringResource(R.string.save))
                }

                SaveStatus.Saving -> {
                    CircularProgressIndicator()
                }

                SaveStatus.Saved -> {
                    LaunchedEffect(Unit) {
                        navigateBack()
                    }
                }
            }
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