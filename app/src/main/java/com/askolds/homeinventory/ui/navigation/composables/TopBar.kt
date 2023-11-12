package com.askolds.homeinventory.ui.navigation.composables

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.askolds.homeinventory.featureHome.ui.NavigationHome
import com.askolds.homeinventory.featureHome.ui.list.HomeListViewModel
import com.askolds.homeinventory.ui.navigation.appbars.AppBarsScrollBehavior
import com.askolds.homeinventory.ui.navigation.appbars.CustomSearchBar

@Composable
fun TopBar(navController: NavController, scrollBehavior: AppBarsScrollBehavior) {
    TopBarContent(
        navController,
        scrollBehavior
    )
}

@Composable
fun TopBarContent(
    navController: NavController,
    scrollBehavior: AppBarsScrollBehavior,
    modifier: Modifier = Modifier
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    when (currentDestination?.route) {
        NavigationHome.List.route -> {
            val parentEntry = remember(navBackStackEntry) {
                navController.getBackStackEntry(NavigationHome.List.route)
            }
            val homeListViewModel = hiltViewModel<HomeListViewModel>(parentEntry)
            SearchBar(
                homeListViewModel.query,
                homeListViewModel::search,
                scrollBehavior
            )
        }
    }


}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    scrollBehavior: AppBarsScrollBehavior
) {
    var isFocused by remember {
        mutableStateOf(false)
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    CustomSearchBar(
        query = query,
        onQueryChange = { onQueryChange(it) },
        onSearch = { },
        active = false,
        onActiveChange = { },
        scrollBehavior = scrollBehavior,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        leadingIcon = {
            if (isFocused) {
                IconButton(onClick = { keyboardController?.hide(); focusManager.clearFocus() }) {
                    Icon(imageVector = Icons.Outlined.ArrowBack, contentDescription = "Exit search")
                }
            }  else {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Open search")
            }
        }
    ) {}
}