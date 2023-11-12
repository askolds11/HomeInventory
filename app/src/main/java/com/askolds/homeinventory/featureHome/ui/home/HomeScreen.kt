package com.askolds.homeinventory.featureHome.ui.home

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeContent(
        state = viewModel.test.value!!,
        //uiState = viewModel.state.value,
        //event = viewModel::onEvent,
        //navigateBack = navController::popBackStack,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
fun HomeContent(
    state: Int,
    //uiState: HomeCreateState,
    //event: (HomeCreateEvent) -> Unit,
    //navigateBack: () -> Boolean,
    modifier: Modifier = Modifier,
) {
    Text(text = state.toString())
}

//@Preview
//@Composable
//fun HomeContentPreview() {
//    HomeInventoryTheme {
//        HomeContent(
//            //uiState = HomeCreateState(),
//            //event = {},
//            //navigateBack = { false },
//            //Modifier.fillMaxSize())
//    }
//}