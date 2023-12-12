package com.askolds.homeinventory.featureThing.domain.usecase

import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureThing.data.repository.ThingRepository
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.featureThing.domain.model.toThingListItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetListFlow(
    private val repository: ThingRepository,
    private val imageRepository: ImageRepository
) {
    operator fun invoke(homeId: Int, parentId: Int?): Flow<List<ThingListItem>> {
        return repository.getList(homeId, parentId).map { items ->
            items.map { item ->
                val imageUri =
                    if (item.imageId != null)
                        imageRepository.getById(item.imageId)?.imageUri
                    else
                        null
                item.toThingListItem(imageUri)
            }
        }
    }
}