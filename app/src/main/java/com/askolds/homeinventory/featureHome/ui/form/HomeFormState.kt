package com.askolds.homeinventory.featureHome.ui.form

import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.ui.SaveStatus

data class HomeFormState (
    val home: Home = Home(id = 0, name = "", image = null),
    val nameValidation: ValidateName.ERROR? = null,
    val saveEnabled: Boolean = false,
    val saveStatus: SaveStatus = SaveStatus.None,
)