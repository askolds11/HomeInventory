package com.askolds.homeinventory.featureThing.ui.form

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material.ripple.LocalRippleTheme
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.ui.ImagePickerCard
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureThing.domain.usecase.thing.validation.ValidateName
import com.askolds.homeinventory.featureThing.ui.NavigationHomeThing
import com.askolds.homeinventory.ui.DarkLightPreviews
import com.askolds.homeinventory.ui.PreviewScaffold
import com.askolds.homeinventory.ui.SaveStatus
import com.askolds.homeinventory.ui.SegmentedButton
import com.askolds.homeinventory.ui.SegmentedButtonDefaults
import com.askolds.homeinventory.ui.SegmentedButtonDefaults.Icon
import com.askolds.homeinventory.ui.SegmentedButtonRow
import com.askolds.homeinventory.ui.getDisabledColor
import com.askolds.homeinventory.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.CollapsingFAB
import com.askolds.homeinventory.ui.navigation.composables.NoRippleTheme
import com.askolds.homeinventory.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.ui.rememberCanNavigate
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.ui.theme.customColors

@Composable
fun ThingFormScreen(
    viewModel: ThingFormViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
) {
    val canNavigate = rememberCanNavigate()

    viewModel.parametersValuesListFlow.collectAsStateWithLifecycle(initialValue = Unit)
    viewModel.parameterSetsValuesListFlow.collectAsStateWithLifecycle(initialValue = Unit)
    viewModel.parametersListFlow.collectAsStateWithLifecycle(initialValue = Unit)
    viewModel.parameterSetsListFlow.collectAsStateWithLifecycle(initialValue = Unit)

    ThingFormContent(
        state = viewModel.state,
        event = viewModel::onEvent,
        appBarsObject = appBarsObject,
        navigateBack = {
            if (canNavigate)
                navController.navigateUp()
        },
        navigateToParametersScreen = {
            if (canNavigate) {
                viewModel.parametersScreenStart()
                navController.navigate(route = NavigationHomeThing.FormParameters.route)
            }
        },
        navigateToParameterSetsScreen = {
            if (canNavigate) {
                viewModel.parameterSetsScreenStart()
                navController.navigate(route = NavigationHomeThing.FormParameterSets.route)
            }
        },
        modifier = Modifier.fillMaxSize(),
    )
}

@Composable
private fun ThingFormContent(
    state: ThingFormState,
    event: (ThingFormEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateBack: () -> Unit,
    navigateToParametersScreen: () -> Unit,
    navigateToParameterSetsScreen: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        ThingForm(
            state,
            event,
            navigateToParametersScreen,
            navigateToParameterSetsScreen,
            appBarsObject.appBarsState.getContentPadding(true),
            modifier
        )
        ThingFormTopAppBar(
            appBarsObject,
            state.isEditMode,
            navigateBack
        )
        val localFocusManager = LocalFocusManager.current
        ThingFormFAB(
            appBarsObject,
            state.saveStatus,
            state.saveEnabled,
            navigateBack,
            onClick = {
                event(ThingFormEvent.Submit)
                localFocusManager.clearFocus()
            }
        )
    }
}

@Composable
private fun ThingFormTopAppBar(
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
private fun BoxScope.ThingFormFAB(
    appBarsObject: AppBarsObject,
    saveStatus: SaveStatus,
    enabled: Boolean,
    navigateBack: () -> Unit,
    onClick: () -> Unit,
) {
    val isEnabled =
        (saveStatus is SaveStatus.None || saveStatus is SaveStatus.Failed)
                && enabled

    fun disabledColor(color: Color): Color {
        return if (isEnabled)
            color
        else
            color.getDisabledColor()
    }

    CompositionLocalProvider(
        // bugged on API 33
        LocalRippleTheme provides if (isEnabled) LocalRippleTheme.current else NoRippleTheme
    ) {
        CollapsingFAB(
            onClick = {
                if (isEnabled)
                    onClick()
            },
            appBarsState = appBarsObject.appBarsState,
            containerColor = disabledColor(MaterialTheme.customColors.successContainer),
            contentColor = disabledColor(MaterialTheme.customColors.onSuccessContainer),
            modifier = Modifier
                .align(Alignment.BottomEnd),
            paddingValues = PaddingValues(end = 16.dp, bottom = 16.dp)
        ) {
            when (saveStatus) {
                SaveStatus.None, SaveStatus.Failed -> {
                    Icon(
                        Icons.Filled.Done,
                        "Save thing"
                    )
                }

                SaveStatus.Saving -> {
                    CircularProgressIndicator(
                        color = disabledColor(MaterialTheme.customColors.onSuccessContainer),
                    )
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

@Composable
private fun ThingForm(
    state: ThingFormState,
    event: (ThingFormEvent) -> Unit,
    navigateToParametersScreen: () -> Unit,
    navigateToParameterSetsScreen: () -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
        contentPadding = contentPadding,
    ) {
        val paddingModifier = Modifier.padding(horizontal = 16.dp)
        item {
            ThingFormContainerButton(
                isContainer = state.thing.isContainer,
                onClick = { value -> event(ThingFormEvent.IsContainerChanged(value)) },
                modifier = paddingModifier
            )
        }
        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            ThingFormImagePickerCard(
                image = state.image,
                isContainer = state.thing.isContainer,
                imageChanged = { uri -> event(ThingFormEvent.ImageChanged(uri)) },
                modifier = paddingModifier.height(128.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            OutlinedTextField(
                value = state.thing.name,
                onValueChange = { event(ThingFormEvent.NameChanged(it)) },
                label = { Text(stringResource(R.string.home_name)) },
                isError = state.nameValidation != null,
                supportingText = {
                    val text = when (state.nameValidation) {
                        ValidateName.ERROR.NULL_OR_BLANK -> stringResource(
                            R.string.validation_required, stringResource(R.string.thing_name)
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

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            ThingFormSimpleHeader("Parameters", "Choose parameters", navigateToParametersScreen)
        }

        item {
            Spacer(modifier = Modifier.height(8.dp))
        }

        itemsIndexed(
            items = state.thingParameters,
            key = { _, thingParameter -> "param-${thingParameter.parameterId}" }
        ) { index, thingParameter ->
            ThingFormParameterValueItemRow(
                parameterName = thingParameter.parameterName,
                value = thingParameter.value,
                onValueChanged = { newValue ->
                    event(ThingFormEvent.ParameterValueChanged(
                        index, newValue
                    ))
                },
                Modifier.padding(start = 24.dp, end = 16.dp)
            )

            if (index < state.thingParameters.lastIndex) {
                Divider(
                    color = MaterialTheme.colorScheme.outline,
                    modifier = Modifier
                        .padding(start = 20.dp, end = 16.dp)
                        .padding(vertical = 8.dp)
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(12.dp))
        }

        item {
            ThingFormSimpleHeader("Parameter sets", "Choose parameter sets", navigateToParameterSetsScreen)
        }

        state.thingParameterSets.forEachIndexed { parameterSetIndex, parameterSet ->
            item {
                ThingFormParameterSetHeaderItemRow(parameterSetName = parameterSet.parameterSetName)
            }
            itemsIndexed(
                items = parameterSet.thingParameters,
                key = { _, thingSetParameter ->
                    "paramSet-${parameterSet.parameterSetId}-${thingSetParameter.parameterId}"
                }
            ) {parameterSetParameterIndex, thingSetParameter ->
                ThingFormParameterValueItemRow(
                    parameterName = thingSetParameter.parameterName,
                    value = thingSetParameter.value,
                    onValueChanged = { newValue ->
                        event(ThingFormEvent.ParameterSetParameterValueChanged(
                            parameterSetIndex, parameterSetParameterIndex, newValue
                        ))
                    },
                    Modifier.padding(start = 24.dp, end = 16.dp)
                )

                if (parameterSetParameterIndex < state.thingParameters.lastIndex) {
                    Divider(
                        color = MaterialTheme.colorScheme.outline,
                        modifier = Modifier
                            .padding(start = 20.dp, end = 16.dp)
                            .padding(vertical = 8.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun ThingFormContainerButton(
    onClick: (value: Boolean) -> Unit,
    isContainer: Boolean,
    modifier: Modifier = Modifier
) {
    SegmentedButtonRow(
        modifier = modifier
            .fillMaxWidth()
            .height(60.dp)
    ) {
        data class ButtonItem(
            val selected: Boolean,
            val value: Boolean,
            val onClick: () -> Unit = { onClick(value) },
            val activeIcon: ImageVector,
            val inactiveIcon: ImageVector,
            val text: String
        )

        val items = listOf(
            ButtonItem(
                selected = isContainer,
                value = true,
                activeIcon = ImageVector.vectorResource(R.drawable.package_2_filled),
                inactiveIcon = ImageVector.vectorResource(R.drawable.package_2_outlined),
                text = "Container",
            ),
            ButtonItem(
                selected = !isContainer,
                value = false,
                activeIcon = Icons.Filled.Hardware,
                inactiveIcon = Icons.Outlined.Hardware,
                text = "Thing",
            ),
        )
        items.forEachIndexed { index, item ->
            SegmentedButton(
                selected = item.selected,
                onClick = item.onClick,
                shape = SegmentedButtonDefaults.itemShape(index = index, count = items.size),
                icon = {
                    Icon(
                        active = item.selected,
                        activeContent = {
                            Icon(
                                imageVector = item.activeIcon,
                                contentDescription = null
                            )
                        },
                        inactiveContent = {
                            Icon(
                                imageVector = item.inactiveIcon,
                                contentDescription = null
                            )
                        }
                    )
                },
                modifier = Modifier.fillMaxHeight()
            ) {
                Text(item.text)
            }
        }
    }
}

@Composable
private fun ThingFormImagePickerCard(
    image: Image?,
    isContainer: Boolean,
    imageChanged: (uri: Uri?) -> Unit,
    modifier: Modifier = Modifier
) {
    ImagePickerCard(
        imageText = image?.fileName ?: "",
        image = {
            val imageModifier = Modifier
                .aspectRatio(3f / 4f)
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                    RoundedCornerShape(8.dp)
                )
                //.weight(2f)

            if (image == null) {
                Image(
                    imageVector =
                    if (isContainer)
                        ImageVector.vectorResource(R.drawable.package_2_outlined)
                    else
                        Icons.Outlined.Hardware,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                    modifier = imageModifier.padding(8.dp)
                )
            } else {
                AsyncImage(
                    model = image.imageUri,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                    //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
                    modifier = imageModifier
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        },
        imageAction = imageChanged,
        modifier
    )
}

@Composable
private fun ThingFormSimpleHeader(
    text: String,
    contentDescription: String,
    onClick: () -> Unit,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.weight(1f))
        IconButton(
            onClick = onClick,
            modifier = Modifier
                .padding(horizontal = 12.dp)
        ) {
            Icon(
                Icons.Filled.Edit,
                contentDescription,
            )
        }
    }
}

@Composable
private fun ThingFormParameterValueItemRow(
    parameterName: String,
    value: String,
    onValueChanged: (value: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Text(parameterName)
        }
        Spacer(Modifier.height(4.dp))
        Row(Modifier.fillMaxWidth()) {
            TextField(
                value = value,
                onValueChange = onValueChanged,
                placeholder = { Text("Enter value...") },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun ThingFormParameterSetHeaderItemRow(
    parameterSetName: String,
    modifier: Modifier = Modifier
) {
    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth()) {
            Text(parameterSetName)
        }
        Spacer(Modifier.height(4.dp))
    }
}

@DarkLightPreviews
@Composable
private fun ThingFormContentPreview() {
    HomeInventoryTheme {
        val thingParameters = (1..2).map {
            ThingParameter(
                id = it,
                value = "Value $it",
                parameterId = it,
                parameterSetId = null,
                parameterSetParameterId = null,
                thingParameterSetId = null,
                parameterName = "Parameter $it"
            )
        }
        val state = ThingFormState(thingParameters = thingParameters.toMutableStateList())
        val appBarsObject = getPreviewAppBarsObject()
        PreviewScaffold(appBarsObject = appBarsObject) {
            ThingFormContent(
                state = state,
                event = {},
                appBarsObject = appBarsObject,
                navigateBack = { },
                navigateToParametersScreen = { },
                navigateToParameterSetsScreen = { },
                Modifier.fillMaxSize()
            )
        }
    }
}