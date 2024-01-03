package com.askolds.homeinventory.featureHome.domain.usecase.home

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository
import com.askolds.homeinventory.featureHome.domain.HomeConstants
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureImage.data.repository.ImageRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class Add (
    private val repository: HomeRepository,
    private val imageRepository: ImageRepository
) {
    suspend operator fun invoke(home: Home): Int {
        return withContext(Dispatchers.IO) {
            var homeWithImage = home
            // if image exists, change imageId
            if (home.image != null) {
                val imageId =
                    imageRepository.insert(home.image.toEntity(), HomeConstants.homeImageSubfolder)
                homeWithImage = home.copy(
                    image = home.image.copy(
                        id = imageId
                    )
                )
            }

            return@withContext repository.insert(homeWithImage.toEntity())
        }
    }
}