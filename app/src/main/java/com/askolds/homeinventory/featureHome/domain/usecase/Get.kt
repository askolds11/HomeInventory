package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.domain.model.toHome
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.domain.model.toImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Get(
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(homeId: Int): Home? {
        return withContext (Dispatchers.IO) {
            return@withContext repository.getById(homeId)?.let { item ->
                val image =
                    if (item.imageId != null)
                        imageRepository.getById(item.imageId)?.toImage()
                    else
                        null
                item.toHome(image)
            }
        }
    }
}