package com.askolds.homeinventory.featureHome.ui.formScreen

import android.net.Uri
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.DarkLightPreviews
import com.askolds.homeinventory.core.ui.fab.SaveFAB
import com.askolds.homeinventory.core.ui.getPreviewAppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.composables.TopAppBar
import com.askolds.homeinventory.core.ui.rememberCanNavigate
import com.askolds.homeinventory.core.ui.theme.HomeInventoryTheme
import com.askolds.homeinventory.featureHome.domain.usecase.home.validation.ValidateName
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.ui.ImagePickerCard

@Composable
fun HomeFormScreen(
    viewModel: HomeFormViewModel,
    appBarsObject: AppBarsObject,
    navController: NavController,
) {
    val canNavigate = rememberCanNavigate()

    HomeFormContent(
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
fun HomeFormContent(
    state: HomeFormState,
    event: (HomeFormEvent) -> Unit,
    appBarsObject: AppBarsObject,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(Modifier.fillMaxSize()) {
        HomeForm(
            state = state,
            event = event,
            contentPadding = appBarsObject.appBarsState.getContentPadding(true, includeKeyboard = true),
            modifier = modifier
        )
        TopAppBar(
            title = {
                Text(
                    if (!state.isEditMode.value)
                        stringResource(R.string.create_home)
                    else
                        stringResource(R.string.edit_home)
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
            appBarsObject = appBarsObject,
        )
        val localFocusManager = LocalFocusManager.current
        SaveFAB(
            appBarsObject = appBarsObject,
            saveStatus = state.saveStatus.value,
            enabled = state.saveEnabled.value,
            navigateBack = navigateBack,
            onClick = {
                event(HomeFormEvent.Submit)
                localFocusManager.clearFocus()
            }
        )
    }


}
@Composable
fun HomeForm(
    state: HomeFormState,
    event: (HomeFormEvent) -> Unit,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = contentPadding
    ) {
        val paddingModifier = Modifier.padding(horizontal = 16.dp)
        item(key = "imageCard") {
            HomeFormImagePickerCard(
                image = state.home.value.image,
                imageChanged = { uri -> event(HomeFormEvent.ImageChanged(uri))},
                modifier = paddingModifier.height(128.dp)
            )
        }
        item(key = "spacer-1") {
            Spacer(modifier = Modifier.height(16.dp))
        }

        item(key = "nameField") {
            OutlinedTextField(
                value = state.home.value.name,
                onValueChange = { event(HomeFormEvent.NameChanged(it)) },
                label = { Text(stringResource(R.string.home_name)) },
                isError = state.nameValidation.value != null,
                supportingText = {
                    val text = when(state.nameValidation.value) {
                        ValidateName.ERROR.NULL_OR_BLANK -> stringResource(
                            R.string.validation_required, stringResource(R.string.home_name)
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

        item(key = "spacer-2") {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }

}

@Composable
private fun HomeFormImagePickerCard(
    image: Image?,
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

            if (image == null) {
                Image(
                    imageVector = Icons.Outlined.Home,
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
                    modifier = imageModifier
                        .clip(RoundedCornerShape(8.dp))
                )
            }
        },
        imageAction = imageChanged,
        modifier
    )
}

@DarkLightPreviews
@Composable
private fun HomeCreateContentPreview() {
    HomeInventoryTheme {
        HomeFormContent(
            state = HomeFormState(),
            event = {},
            appBarsObject = getPreviewAppBarsObject(),
            navigateBack = { },
            Modifier.fillMaxSize())
    }
}