package com.askolds.homeinventory.featureParameter.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "parameter_set",
    indices = [
        Index("searchName"),
    ]
)
data class ParameterSetEntity (
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    val searchName: String,
)