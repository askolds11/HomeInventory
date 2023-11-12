package com.askolds.homeinventory.featureHome.ui.create

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.ui.getDrawingPadding
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun HomeCreateScreen(
    viewModel: HomeCreateViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeCreateContent(
        uiState = viewModel.state.value,
        event = viewModel::onEvent,
        navigateBack = { navController.navigateUp() },
        modifier = modifier.fillMaxSize(),
    )

    DisposableEffect(Unit) {
        onDispose {
            viewModel.resetState()
        }
    }
}

@Composable
fun HomeCreateContent(
    uiState: HomeCreateState,
    event: (HomeCreateEvent) -> Unit,
    navigateBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier
        .verticalScroll(rememberScrollState())
        .padding(
            getDrawingPadding(start = 16.dp, end = 16.dp, top = 32.dp)
        )
    ) {
        OutlinedTextField(
            value = uiState.name,
            onValueChange = { event(HomeCreateEvent.NameChanged(it)) },
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
                event(HomeCreateEvent.Submit); localFocusManager.clearFocus()},
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

@Preview
@Composable
fun HomeCreateContentPreview() {
    HomeInventoryTheme {
        HomeCreateContent(
            uiState = HomeCreateState(),
            event = {},
            navigateBack = { false },
            Modifier.fillMaxSize())
    }
}