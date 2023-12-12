package com.askolds.homeinventory.featureHome.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureImage.data.model.ImageEntity

@Entity(
    tableName = "home",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index("searchName"),
        Index("imageId")
    ]
)
data class HomeEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
    val imageId: Int?,
)