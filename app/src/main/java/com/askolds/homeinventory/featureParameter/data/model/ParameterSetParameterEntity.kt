package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parameter_set_parameter",
    foreignKeys = [
        ForeignKey(
            entity = ParameterEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ParameterSetEntity::class,
            parentColumns = ["id"],
            childColumns = ["parameterSetId"],
            onDelete = ForeignKey.CASCADE
        ),
    ],
    indices = [
        Index("parameterId"),
        Index("parameterSetId")
    ]
)
data class ParameterSetParameterEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val parameterId: Int,
    val parameterSetId: Int
)