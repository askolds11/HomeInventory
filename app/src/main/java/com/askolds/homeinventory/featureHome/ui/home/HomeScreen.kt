package com.askolds.homeinventory.featureHome.ui.home

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
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.featureThing.ui.NavigationThing
import com.askolds.homeinventory.featureThing.ui.list.ThingList
import com.askolds.homeinventory.featureThing.ui.list.ThingListEvent
import com.askolds.homeinventory.featureThing.ui.list.ThingListState
import com.askolds.homeinventory.featureThing.ui.list.ThingListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    listViewModel: ThingListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    HomeContent(
        thingListState = listViewModel.state,
        thingListEvent = listViewModel::onEvent,
        navigateToEditHome = {
            navController.navigate(route = NavigationHome.Edit.getRoute(viewModel.state.home.id))
        },
        navigateToThing = { homeId, thingId ->
            navController.navigate(route = NavigationThing.Thing.getRoute(homeId, thingId))
        },
        navigateToCreateThing = { homeId, thingId ->
            navController.navigate(route = NavigationThing.Create.getRoute(homeId, thingId))
        },
        appBarsObject = appBarsObject,
        home = viewModel.state.home,
        modifier = modifier.fillMaxSize(),
    )
}

@Composable
private fun HomeContent(
    thingListState: ThingListState,
    thingListEvent: (ThingListEvent) -> Unit,
    navigateToEditHome: () -> Unit,
    navigateToThing: (homeId: Int, thingId: Int) -> Unit,
    navigateToCreateThing: (homeId: Int, thingId: Int?) -> Unit,
    appBarsObject: AppBarsObject,
    home: Home,
    modifier: Modifier = Modifier,
) {
    ThingList(
        thingListState,
        thingListEvent,
        navigateToThing,
        navigateToCreateThing,
        appBarsObject,
        modifier,
        searchBarTrailingIcon = {
            IconButton(
                onClick = navigateToEditHome,
                Modifier
                    .padding(horizontal = 12.dp)
            ) {
                Icon(
                    Icons.Filled.Edit,
                    "Edit home",
                )
            }
        }
    ) {
        HomeHeader(imageUri = home.image?.imageUri, text = home.name)
        Divider(color = MaterialTheme.colorScheme.outline)
    }
}

@Composable
private fun HomeHeader(
    imageUri: String?,
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
                imageVector = Icons.Outlined.Home,
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

@Preview(showBackground = true)
@Composable
fun HomeHeaderPreview() {
    HomeInventoryTheme {
        HomeHeader(
            null,
            "Tests",
        )
    }
}
//@Preview(showBackground = true)
//@Composable
//fun HomeContentPreview() {
//    val items = (1..7).map { ThingListItem(it, "Item $it", false) }
//    HomeInventoryTheme {
//        HomeContent(
//            thingListState = ThingListState(),
//            thingListEvent = {},
//            navigateToThing = {},
//            navigateToCreateThing = {},
//            appBarsObject = AppBarsObject(
//                rememberAppBarState()
//            )
//            home = Home(0, "Tests"),
//        )
//    }
//}