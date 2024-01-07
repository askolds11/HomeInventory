package com.askolds.homeinventory.core.ui

sealed class SaveStatus {
    data object None: SaveStatus()
    data object Saving: SaveStatus()
    data object Saved: SaveStatus()
    data object Failed: SaveStatus()
}