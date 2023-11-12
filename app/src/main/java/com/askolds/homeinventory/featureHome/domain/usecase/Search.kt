package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.domain.model.toHomeListItem
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Search(
    private val repository: HomeRepository
) {
    operator fun invoke(name: String): Flow<List<HomeListItem>> {
        return repository.search(name).map { items ->
            items.map { item ->
                item.toHomeListItem()
            }
        }
    }
}