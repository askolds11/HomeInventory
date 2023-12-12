package com.askolds.homeinventory.featureHome.ui.home

import com.askolds.homeinventory.featureHome.domain.model.Home

data class HomeState (
    val home: Home = Home(0, "", null),
)