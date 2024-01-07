package com.askolds.homeinventory.featureParameter.ui.parameterSetForm

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Deselect
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.toMutableStateList
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
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.validation.ValidateName
import com.askolds.homeinventory.featureParameter.ui.listItem.SelectableParameterListItemRow

@Composable
fun ParameterSetFormScreen(
    viewModel: ParameterSetFormViewModel,
    appBarsObject: AppBarsObject,
    navController: NavController,
) {
    val canNavigate = rememberCanNavigate()
    ParameterSetFormContent(
        state = viewModel.state,
        event = viewModel::onEvent,
        appBarsObject = appBarsObject,
        navigateBack = {
            if (canNavigate.value)
                navController.navigateUp()
        },
        modifier = Modifier.fillMaxSize()
    )
}

@Composable
private fun ParameterSetFormContent(
    state: ParameterSetFormState,
    event: (ParameterSetFormEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        ParameterSetForm(
            uiState = state,
            event = event,
            contentPadding = appBarsObject.appBarsState.getContentPadding(),
            modifier = modifier
        )
        ParameterSetFormTopAppBar(
            appBarsObject = appBarsObject,
            isEditMode = state.isEditMode,
            navigateBack = navigateBack,
            unselectAll = { event(ParameterSetFormEvent.UnselectAll) }
        )
        val localFocusManager = LocalFocusManager.current

        SaveFAB(
            appBarsObject = appBarsObject,
            saveStatus = state.saveStatus,
            enabled = true,
            navigateBack = navigateBack,
            onClick = { event(ParameterSetFormEvent.Submit); localFocusManager.clearFocus() }
        )
    }
}

@Composable
private fun ParameterSetFormTopAppBar(
    appBarsObject: AppBarsObject,
    isEditMode: Boolean,
    navigateBack: () -> Unit,
    unselectAll: () -> Unit
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
                    stringResource(R.string.navigate_back),
                )
            }
        },
        actions = {
            IconButton(
                onClick = {
                    unselectAll()
                },
                modifier = Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Deselect,
                    stringResource(R.string.unselect_all),
                )
            }
        },
        appBarsObject = appBarsObject
    )
}

@Composable
private fun ParameterSetForm(
    uiState: ParameterSetFormState,
    event: (ParameterSetFormEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        val parameterList = uiState.parameterList
        item {
            OutlinedTextField(
                value = uiState.parameterSet.name,
                onValueChange = { event(ParameterSetFormEvent.NameChanged(it)) },
                label = { Text(stringResource(R.string.parameter_set_name)) },
                isError = uiState.nameValidation != null,
                supportingText = {
                    val text = when (uiState.nameValidation) {
                        ValidateName.ERROR.NULL_OR_BLANK -> stringResource(
                            R.string.validation_required, stringResource(R.string.parameter_set_name)
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
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
            )
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        item {
            Text(
                stringResource(R.string.parameters),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        itemsIndexed(
            items = parameterList, key = { _, parameter -> parameter.id }
        ) { index, parameter ->
            val clickModifier = Modifier.clickable(
                interactionSource = remember { MutableInteractionSource() },
                // Don't show click when selecting
                indication = null,
                onClick = {
                    event(
                        ParameterSetFormEvent.SelectItem(
                            parameter.id, !parameter.selected, index
                        )
                    )
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
private fun ParameterSetListContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map {
            ParameterListItem(id = it, name = "Item $it", selected = false)
        }
        val state = ParameterSetFormState(parameterList = items.toMutableStateList())
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ParameterSetFormContent(
                state = state,
                event = {},
                appBarsObject = appBarsObject,
                navigateBack = { },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}