package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.domain.usecase.validation.ValidateName

data class HomeUseCases(
    val getList: GetList,
    val add: Add,
    val validateName: ValidateName,
    val search: Search,
)
