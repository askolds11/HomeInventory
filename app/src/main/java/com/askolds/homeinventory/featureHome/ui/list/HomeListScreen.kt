package com.askolds.homeinventory.featureHome.ui.list

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.House
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.ui.navigation.composables.CreateFAB
import com.askolds.homeinventory.ui.navigation.composables.SearchBar
import com.askolds.homeinventory.ui.theme.HomeInventoryTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun HomeListScreen(
    viewModel: HomeListViewModel,
    navController: NavController,
    appBarsObject: AppBarsObject,
    modifier: Modifier = Modifier
) {
    val lifecycleOwner = LocalLifecycleOwner.current
    val currentState = lifecycleOwner.lifecycle.currentState
    val canNavigate = remember(currentState) {
        currentState.isAtLeast(Lifecycle.State.RESUMED)
    }
//    Scaffold(
//        topBar = {
//            SearchBar(
//                query = viewModel.query,
//                onQueryChange = viewModel::search,
//                appBarsObject = appBarsObject)
//        },
//        floatingActionButton = {
//            CreateFAB(
//                appBarsObject.appBarsState,
//            ) {
//                if (canNavigate)
//                    navController.navigate(route = NavigationHome.Create.route)
//            }
//        },
//        contentWindowInsets = WindowInsets(0, 0, 0, 0)
//    ) {
//        HomeListContent(
//            homeList = viewModel.state.value.homeList,
//            navigateToHome = { homeId ->
//                navController.navigate(route = NavigationHome.Home.getRoute(homeId))
//            },
//            canNavigate = canNavigate,
//            contentPadding = PaddingValues(
//                top = appBarsObject.appBarsState.topPadding,
//                bottom = appBarsObject.appBarsState.bottomPadding,
//            ),
//            modifier = modifier.fillMaxSize(),
//        )
//    }

    Box(Modifier.fillMaxSize()) {
        SearchBar(
            query = viewModel.query,
            onQueryChange = { viewModel.search(it) },
            appBarsObject = appBarsObject)
        HomeListContent(
            homeList = viewModel.state.value.homeList,
            navigateToHome = { homeId ->
                navController.navigate(route = NavigationHome.Home.getRoute(homeId))
            },
            canNavigate = canNavigate,
            contentPadding = PaddingValues(
                top = appBarsObject.appBarsState.topPadding,
                bottom = appBarsObject.appBarsState.bottomPadding,
            ),
            modifier = modifier.fillMaxSize(),
        )
        CreateFAB(
            appBarsObject.appBarsState,
            Modifier
                .padding(end = 16.dp, bottom = 16.dp)
                .align(Alignment.BottomEnd)
        ) {
            if (canNavigate)
                navController.navigate(route = NavigationHome.Create.route)
        }
    }

}

@Composable
fun HomeListContent(
    homeList: List<HomeListItem>,
    navigateToHome: (Int) -> Unit,
    canNavigate: Boolean,
    contentPadding: PaddingValues,
    modifier: Modifier = Modifier,
) {
    //NavBarVisibility(lazyListState, 1000)

    val lazyListState = rememberLazyListState()
    //val bottomBarNestedScrollConnection = rememberBottomBarNestedScrollConnection(lazyListState)


    LazyColumn(
        modifier = modifier
            .fillMaxSize(),
            //.nestedScroll(bottomBarNestedScrollConnection),
        contentPadding = PaddingValues(
            //start = WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current),
            //end = WindowInsets.safeContent.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current),
            bottom = contentPadding.calculateBottomPadding(),
            top = contentPadding.calculateTopPadding()
        ),
        state = lazyListState
    )  {
        itemsIndexed(items = homeList, key = { _, home -> home.id }) { index, home ->
            val navigateModifier =
                if (canNavigate) {
                    Modifier.clickable {
                        navigateToHome(home.id)
                    }
                } else {
                    Modifier
                }

            HomeListItem(
                icon = null,
                text = home.name,
                modifier = navigateModifier
            )
            if (index < homeList.lastIndex) {
                Divider(color = MaterialTheme.colorScheme.outline)
            }
        }
    }

}

@Composable
fun HomeListItem(
    icon: String?,
    text: String,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .padding(horizontal = 12.dp, vertical = 8.dp)
            .fillMaxWidth()
            .height(44.dp),
    ) {
        Image(
            imageVector = Icons.Outlined.House,
            contentScale = ContentScale.Crop,
            contentDescription = null,
            colorFilter = if (icon == null) ColorFilter.tint(MaterialTheme.colorScheme.onBackground) else null,
            modifier = Modifier
                .fillMaxHeight()
                .aspectRatio(1f)
                .border(
                    BorderStroke(2.dp, MaterialTheme.colorScheme.outline),
                    RoundedCornerShape(8.dp)
                )
        )
        Text(
            text,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.padding(horizontal = 8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeListContentPreview() {
    val items = (1..5).map {HomeListItem(it, "Item $it")}
    HomeInventoryTheme {
        HomeListContent(
            items,
            {},
            canNavigate = true,
            PaddingValues(vertical = 60.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun HomeListItemPreview() {
    HomeInventoryTheme {
        HomeListItem(icon = null, text = "Tests123")
    }
}