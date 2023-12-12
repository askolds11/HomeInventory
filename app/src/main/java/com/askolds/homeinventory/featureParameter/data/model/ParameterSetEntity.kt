package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureImage.data.model.ImageEntity

@Entity(
    tableName = "parameter_set",
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
data class ParameterSetEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
    val imageId: Int?
)