package com.askolds.homeinventory.featureHome.domain.usecase

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.domain.model.toHome
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import com.askolds.homeinventory.featureImage.domain.model.toImage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class GetFlow(
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    operator fun invoke(homeId: Int): Flow<Home> {
        return repository.getFlowById(homeId).map { item ->
            val image =
                if (item.imageId != null)
                    imageRepository.getById(item.imageId)?.toImage()
                else
                    null
            item.toHome(image)
        }
    }
}