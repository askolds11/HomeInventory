package com.askolds.homeinventory.featureHome.ui.formScreen

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.domain.usecase.home.validation.ValidateName
import com.askolds.homeinventory.core.ui.SaveStatus

@Stable
class HomeFormState (
    val home: MutableState<Home> = mutableStateOf(Home(id = 0, name = "", image = null)),
    val nameValidation: MutableState<ValidateName.ERROR?> = mutableStateOf(null),
    val saveEnabled: MutableState<Boolean> = mutableStateOf(false),
    val isEditMode: MutableState<Boolean> = mutableStateOf(false),
    val saveStatus: MutableState<SaveStatus> = mutableStateOf(SaveStatus.None),
)