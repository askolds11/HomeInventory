package com.askolds.homeinventory.featureObject.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureHome.data.model.HomeEntity

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
    ]
)
data class ThingEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val isContainer: Boolean,
    val parentId: Int?,
    val homeId: Int,
)