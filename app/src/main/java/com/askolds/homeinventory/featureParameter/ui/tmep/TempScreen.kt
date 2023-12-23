package com.askolds.homeinventory.featureParameter.ui.tmep

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
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.R
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterList
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListEvent
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListState
import com.askolds.homeinventory.featureParameter.ui.parameterList.ParameterListViewModel
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.ui.NavigationThing
import com.askolds.homeinventory.featureThing.ui.list.ThingList
import com.askolds.homeinventory.featureThing.ui.list.ThingListEvent
import com.askolds.homeinventory.featureThing.ui.list.ThingListState
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject

@Composable
fun TempScreen(
    viewModel: TempViewModel,
    listViewModel: ParameterListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    TempContent(
        parameterListState = listViewModel.state,
        parameterListEvent = listViewModel::onEvent,
//        navigateToThing = { homeId, thingId ->
//            navController.navigate(route = NavigationThing.Thing.getRoute(homeId, thingId))
//        },
//        navigateToCreateThing = { homeId, thingId ->
//            navController.navigate(route = NavigationThing.Create.getRoute(homeId, thingId))
//        },
//        navigateToEditThing = {
//            navController.navigate(route = NavigationThing.Edit.getRoute(viewModel.state.thing.id))
//        },
        appBarsObject = appBarsObject,
//        thing = viewModel.state.thing,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun TempContent(
    parameterListState: ParameterListState,
    parameterListEvent: (ParameterListEvent) -> Unit,
//    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
//    navigateToCreateThing: (homeId: Int, thingId: Int?) -> Unit,
//    navigateToEditThing: () -> Unit,
    appBarsObject: AppBarsObject,
//    thing: Thing,
    modifier: Modifier = Modifier,
) {
    ParameterList(
        parameterListState,
        parameterListEvent,
//        navigateToThing,
//        navigateToCreateThing,
        appBarsObject,
        modifier,
        searchBarTrailingIcon = {
            IconButton(
                onClick = { }, //navigateToEditThing,
                Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    "Edit parameter",
                )
            }
        }
    ) {
//        ThingHeader(imageUri = thing.imageUri, isContainer = thing.isContainer, text = thing.name)
//        Divider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun ThingHeader(
    imageUri: String?,
    isContainer: Boolean,
    text: String,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier
            .padding(horizontal = 12.dp)
            .padding(top = 8.dp, bottom = 12.dp)
            .fillMaxWidth()
    ) {
        val imageModifier = Modifier
            .border(
                BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                RoundedCornerShape(8.dp)
            )
            .weight(1f)
        if (imageUri == null) {
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
                model = imageUri,
                contentScale = ContentScale.Crop,
                contentDescription = null,
                //colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
                modifier = imageModifier
                    .clip(RoundedCornerShape(8.dp))
            )
        }
        Text(
            text,
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier
                .padding(horizontal = 8.dp)
                .weight(3f)
        )
    }
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