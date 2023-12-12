package com.askolds.homeinventory.featureImage.data.repository

import com.askolds.homeinventory.featureImage.data.model.ImageEntity

interface ImageRepository {
    suspend fun insert(image: ImageEntity, subfolder: String): Int
    suspend fun getById(id: Int): ImageEntity?
    suspend fun delete(image: ImageEntity)
    suspend fun deleteById(id: Int)
}