package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.askolds.homeinventory.featureImage.data.model.ImageEntity

@Entity(
    tableName = "parameter_set_parameter",
    foreignKeys = [
        ForeignKey(
            entity = ImageEntity::class,
            parentColumns = ["id"],
            childColumns = ["imageId"],
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
        Index("searchName"),
        Index("imageId")
    ]
)
data class ParameterSetParameterEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val parameterId: Int,
    val parameterSetId: Int
)