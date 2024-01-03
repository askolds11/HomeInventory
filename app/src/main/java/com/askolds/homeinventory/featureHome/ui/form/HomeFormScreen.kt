package com.askolds.homeinventory.featureHome.ui.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureHome.domain.usecase.home.validation.ValidateName
import com.askolds.homeinventory.featureImage.ui.ImagePickerCard
import com.askolds.homeinventory.ui.SaveStatus
import com.askolds.homeinventory.ui.getDrawingPadding
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun HomeFormScreen(
    viewModel: HomeFormViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeForm(
        uiState = viewModel.state,
        event = viewModel::onEvent,
        navigateBack = { navController.navigateUp() },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
fun HomeForm(
    uiState: HomeFormState,
    event: (HomeFormEvent) -> Unit,
    navigateBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(
            getDrawingPadding(start = 16.dp, end = 16.dp, top = 32.dp)
        )
    ) {
        ImagePickerCard(
            imageText = uiState.home.image?.fileName ?: "",
            image = {
                val imageModifier = Modifier
                    .aspectRatio(3f / 4f)
                    .border(
                        BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                        RoundedCornerShape(8.dp)
                    )
                    .weight(2f)
                if (uiState.home.image == null) {
                    Image(
                        imageVector = Icons.Outlined.Home,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
                        modifier = imageModifier.padding(8.dp)
                    )
                } else {
                    AsyncImage(
                        model = uiState.home.image.imageUri,
                        contentScale = ContentScale.Crop,
                        contentDescription = null,
                        modifier = imageModifier
                            .clip(RoundedCornerShape(8.dp))
                    )
                }
            },
            imageAction = { uri -> event(HomeFormEvent.ImageChanged(uri))}
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = uiState.home.name,
            onValueChange = { event(HomeFormEvent.NameChanged(it)) },
            label = { Text(stringResource(R.string.home_name)) },
            isError = uiState.nameValidation != null,
            supportingText = {
                val text = when(uiState.nameValidation) {
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
                event(HomeFormEvent.Submit); localFocusManager.clearFocus()},
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
private fun HomeCreateContentPreview() {
    HomeInventoryTheme {
        HomeForm(
            uiState = HomeFormState(),
            event = {},
            navigateBack = { false },
            Modifier.fillMaxSize()
        )
    }
}