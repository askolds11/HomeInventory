package com.askolds.homeinventory.featureHome.domain.usecase.home

import com.askolds.homeinventory.featureHome.domain.usecase.home.validation.ValidateName

data class HomeUseCases(
    val getListFlow: GetListFlow,
    val getFlow: GetFlow,
    val add: Add,
    val update: Update,
    val validateName: ValidateName,
    val search: Search,
    val deleteList: DeleteList
)
