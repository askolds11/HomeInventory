package com.askolds.homeinventory.core.ui.navigation.composables

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.askolds.homeinventory.R
import com.askolds.homeinventory.core.ui.navigation.appbars.AppBarsObject
import com.askolds.homeinventory.core.ui.navigation.appbars.CustomSearchBar
import com.askolds.homeinventory.core.ui.navigation.appbars.CustomTopAppBar

/**
 * General search bar
 * @param query Query string
 * @param onQueryChange On query changed lambda
 * @param appBarsObject App bars object
 */
@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    clearQuery: () -> Unit,
    appBarsObject: AppBarsObject,
    trailingIcon: @Composable (() -> Unit)? = null,
    placeholderText: String? = null,
) {
    var isFocused by remember {
        mutableStateOf(false)
    }
    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    BackHandler(
        enabled = isFocused
    ) {
        keyboardController?.hide()
        focusManager.clearFocus()
        clearQuery()
    }


    CustomSearchBar(
        query = query,
        onQueryChange = onQueryChange,
        onSearch = { },
        active = false,
        onActiveChange = { },
        appBarsObject = appBarsObject,
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxWidth()
            .onFocusChanged { isFocused = it.isFocused },
        placeholder = { Text(placeholderText ?: stringResource(R.string.search_placeholder))},
        leadingIcon = {
            if (isFocused) {
                IconButton(
                    onClick = {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                        clearQuery()
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowBack,
                        contentDescription = "Exit search"
                    )
                }
            }  else {
                Icon(imageVector = Icons.Outlined.Search, contentDescription = "Open search")
            }
        },
        trailingIcon = {
            if (query.isNotEmpty()) {
                IconButton(
                    onClick = {
                        clearQuery()
                    }) {
                    Icon(
                        imageVector = Icons.Outlined.Clear,
                        contentDescription = "Clear query"
                    )
                }
            } else {
                trailingIcon?.invoke()
            }
        }
    ) {}
}

/**
 * General top bar
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: @Composable () -> Unit,
    navigationIcon: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {},
    appBarsObject: AppBarsObject
) {
    CustomTopAppBar(
        title = title,
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.background,
            titleContentColor = MaterialTheme.colorScheme.onBackground,
        ),
        navigationIcon = navigationIcon,
        actions = actions,
        appBarsObject = appBarsObject
    )
}