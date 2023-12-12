package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.domain.toSearchable
import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.domain.model.toHomeListItem
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class Search(
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    operator fun invoke(name: String): Flow<List<HomeListItem>> {
        return repository.search(name.toSearchable()).map { items ->
            items.map { item ->
                val imageUri =
                    if (item.imageId != null)
                        imageRepository.getById(item.imageId)?.imageUri
                    else
                        null
                item.toHomeListItem(imageUri)
            }
        }
    }
}