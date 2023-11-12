package com.askolds.homeinventory.ui.navigation.appbars

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AppBarsViewModel @Inject constructor(
    val appBarsState: AppBarsState
) : ViewModel()