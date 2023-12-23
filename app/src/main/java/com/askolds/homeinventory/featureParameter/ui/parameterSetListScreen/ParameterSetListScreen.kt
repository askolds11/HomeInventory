package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.outlined.Hardware
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterList
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListEvent
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListState
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListViewModel
import com.askolds.homeinventory.featureParameter.ui.parameterSetList.ParameterSetList
import com.askolds.homeinventory.featureParameter.ui.parameterSetList.ParameterSetListEvent
import com.askolds.homeinventory.featureParameter.ui.parameterSetList.ParameterSetListState
import com.askolds.homeinventory.featureParameter.ui.parameterSetList.ParameterSetListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarState
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsDefaults
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsScrollBehavior
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsState
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun ParameterSetListScreen(
    viewModel: ParameterSetListScreenViewModel,
    listViewModel: ParameterSetListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    ParameterSetListScreenContent(
        parameterListState = listViewModel.state,
        parameterListEvent = listViewModel::onEvent,
        navigateToParameterListScreen = {
            //navController.navigate(route = NavigationThing.Thing.getRoute(homeId, thingId))
        },
        navigateToParameterSet = { parameterSetId ->
            //navController.navigate(route = NavigationThing.Create.getRoute(homeId, thingId))
        },
        navigateToCreateParameterSet = {
            //navController.navigate(route = NavigationThing.Edit.getRoute(viewModel.state.thing.id))
        },
        appBarsObject = appBarsObject,
//        thing = viewModel.state.thing,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun ParameterSetListScreenContent(
    parameterListState: ParameterSetListState,
    parameterListEvent: (ParameterSetListEvent) -> Unit,
    navigateToParameterListScreen: () -> Unit,
    navigateToParameterSet: (parameterId: Int) -> Unit,
    navigateToCreateParameterSet: () -> Unit,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier,
) {
    ParameterSetList(
        parameterListState,
        parameterListEvent,
//        navigateToThing,
//        navigateToCreateThing,
        appBarsObject,
        modifier,
        searchBarTrailingIcon = { }
    ) {
//        ThingHeader(imageUri = thing.imageUri, isContainer = thing.isContainer, text = thing.name)
//        Divider(color = MaterialTheme.colorScheme.outline)
    }
}

@Preview(showBackground = true)
@Composable
fun ParameterSetListScreenContentPreview() {
    HomeInventoryTheme {
        val items = (1..5).map { ParameterListItem(it, "Item $it") }
        val state = ParameterSetListState(items.toMutableStateList())

        ParameterSetListScreenContent(
            parameterListState = state,
            parameterListEvent = { },
            navigateToParameterListScreen = { },
            navigateToParameterSet = { },
            navigateToCreateParameterSet = { },
            appBarsObject = AppBarsObject(
                AppBarsState(
                    AppBarState(0f, 0f, 0f),
                    AppBarState(0f, 0f, 0f)
                ),
                AppBarsDefaults.exitAlwaysScrollBehavior()
            )
        )
    }
}