package com.askolds.homeinventory.ui.home

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeContent
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavController
import com.askolds.homeinventory.R
import com.askolds.homeinventory.ui.navigation.LocalNavBar
import com.askolds.homeinventory.ui.navigation.NavBarVisibility

@Composable
fun HomeListScreen(
    viewModel: HomeListViewModel,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    HomeListContent(
        modifier = modifier,
    )
}

@Composable
fun HomeListContent(
    modifier: Modifier = Modifier,
    lazyListState: LazyListState = rememberLazyListState()
) {
    NavBarVisibility(lazyListState, 100)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = WindowInsets.safeContent.asPaddingValues().calculateStartPadding(LocalLayoutDirection.current),
            end = WindowInsets.safeContent.asPaddingValues().calculateEndPadding(LocalLayoutDirection.current),
            bottom = LocalNavBar.current.bottomBarPadding,
            top = WindowInsets.safeContent.asPaddingValues().calculateTopPadding()
        ),
        state = lazyListState
    ) {
        item {
            Text(
                text = stringResource(R.string.app_name)
            )
        }
        items(100) {
            Text(
                text = "Teksts $it"
            )
        }
    }
}