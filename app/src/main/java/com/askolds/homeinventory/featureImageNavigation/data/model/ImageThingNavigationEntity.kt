package com.askolds.homeinventory.featureImageNavigation.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureImage.data.model.ImageEntity
import com.askolds.homeinventory.featureThing.data.model.ThingEntity

@Entity(
    tableName = "image_thing_navigation",
    foreignKeys = [
        ForeignKey(
            entity = ThingEntity::class,
            parentColumns = ["id"],
            childColumns = ["thingId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("thingId"),
        Index("imageId")
    ]
)
data class ImageThingNavigationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val imageId: Int,
    val thingId: Int?,
    val offsetX: Float,
    val offsetY: Float,
    val sizeX: Float,
    val sizeY: Float,
    val zIndex: Int
)