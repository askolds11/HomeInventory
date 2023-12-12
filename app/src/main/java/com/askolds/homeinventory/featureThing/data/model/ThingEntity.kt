package com.askolds.homeinventory.featureThing.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureHome.data.model.HomeEntity
import com.askolds.homeinventory.featureImage.data.model.ImageEntity

@Entity(
    tableName = "thing",
    foreignKeys = [
        ForeignKey(
            entity = ThingEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = HomeEntity::class,
            parentColumns = ["id"],
            childColumns = ["homeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
            onDelete = ForeignKey.SET_NULL
        ),
    ],
    indices = [
        Index("searchName"),
        Index("parentId"),
        Index("homeId"),
        Index("imageId")
    ]
)
data class ThingEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
    val isContainer: Boolean,
    val parentId: Int?,
    val homeId: Int,
    val imageId: Int?
)