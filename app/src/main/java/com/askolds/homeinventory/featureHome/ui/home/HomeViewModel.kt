package com.askolds.homeinventory.featureHome.ui.home

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private val homeId = savedStateHandle.get<Int>("homeId")
    val test = mutableStateOf(homeId)
}