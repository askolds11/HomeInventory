package com.askolds.homeinventory.featureHome.ui.homeScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.askolds.homeinventory.featureHome.domain.model.Home

data class HomeState (
    val home: MutableState<Home> = mutableStateOf(Home(0, "", null)),
)