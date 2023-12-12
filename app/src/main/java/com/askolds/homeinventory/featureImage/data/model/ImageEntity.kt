package com.askolds.homeinventory.featureImage.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "image",
)
data class ImageEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val fileName: String,
    val imageUri: String
)