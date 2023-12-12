package com.askolds.homeinventory.featureThing.ui.form

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Hardware
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureImage.ui.ImagePickerCard
import com.askolds.homeinventory.featureThing.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.ui.SaveStatus
import com.askolds.homeinventory.ui.SegmentedButton
import com.askolds.homeinventory.ui.SegmentedButtonDefaults
import com.askolds.homeinventory.ui.SegmentedButtonDefaults.Icon
import com.askolds.homeinventory.ui.SegmentedButtonRow
import com.askolds.homeinventory.ui.getDrawingPadding
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun ThingFormScreen(
    viewModel: ThingFormViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    ThingFormContent(
        uiState = viewModel.state,
        event = viewModel::onEvent,
        navigateBack = { navController.navigateUp() },
        modifier = modifier.fillMaxSize(),
    )
}

@SuppressLint("Range")
@Composable
private fun ThingFormContent(
    uiState: ThingFormState,
    event: (ThingFormEvent) -> Unit,
    navigateBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(
            getDrawingPadding(start = 16.dp, end = 16.dp, top = 32.dp)
        )
    ) {
        SegmentedButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
        ) {
            data class ButtonItem(
                val selected: Boolean,
                val value: Boolean,
                val onClick: () -> Unit = { event(ThingFormEvent.IsContainerChanged(value)) },
                val activeIcon: ImageVector,
                val inactiveIcon: ImageVector,
                val text: String
            )
            val items = listOf(
                ButtonItem(
                    selected = uiState.thing.isContainer,
                    value = true,
                    activeIcon = ImageVector.vectorResource(R.drawable.package_2_filled),
                    inactiveIcon = ImageVector.vectorResource(R.drawable.package_2_outlined),
                    text = "Container",
                ),
                ButtonItem(
                    selected = !uiState.thing.isContainer,
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
        Spacer(modifier = Modifier.height(16.dp))

        ImagePickerCard(
            imageText = uiState.image?.fileName ?: "",
            image = {
                val imageModifier = Modifier
                    .aspectRatio(3f / 4f)
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                        RoundedCornerShape(8.dp)
                    )
                    .weight(2f)
                if (uiState.image == null) {
                    Image(
                        imageVector =
                        if (uiState.thing.isContainer)
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
                        model = uiState.image.imageUri,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
                        modifier = imageModifier
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            },
            imageAction = { uri -> event(ThingFormEvent.ImageChanged(uri))}
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.thing.name,
            onValueChange = { event(ThingFormEvent.NameChanged(it)) },
            label = { Text(stringResource(R.string.home_name)) },
            isError = uiState.nameValidation != null,
            supportingText = {
                val text = when(uiState.nameValidation) {
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))
        val localFocusManager = LocalFocusManager.current
        Button(
            onClick = {
                event(ThingFormEvent.Submit); localFocusManager.clearFocus(); navigateBack()},
            enabled = uiState.saveEnabled,
            modifier = Modifier.fillMaxWidth()) {
            when (uiState.saveStatus) {
                SaveStatus.None, SaveStatus.Failed -> {
                    Text(stringResource(R.string.save))
                }
                SaveStatus.Saving -> {
                    Text(stringResource(R.string.save))
                    CircularProgressIndicator() //TODO : Make correct sizes!
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

@Preview(showBackground = true)
@Composable
fun ThingCreateContentPreview() {
    HomeInventoryTheme {
        ThingFormContent(
            uiState = ThingFormState(),
            event = {},
            navigateBack = { false },
            Modifier.fillMaxSize())
    }
}