package com.askolds.homeinventory.featureThing.domain.usecase.thing

import com.askolds.homeinventory.core.domain.stripAccents
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.featureThing.domain.model.toThingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Search(
    private val repository: ThingRepository
) {
    /**
     * Searches for things. If parentId and homeId are null, searches all things
     * @param homeId id of home to search in
     * @param parentId id of thing to search in
     * @param name search string
     */
    operator fun invoke(homeId: Int?, parentId: Int?, name: String): Flow<List<ThingListItem>> {
        return repository.search(homeId, parentId, name.stripAccents().lowercase()).map { items ->
            items.map { item ->
                item.toThingListItem()
            }
        }
    }
}