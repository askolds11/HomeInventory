package com.askolds.homeinventory.core.ui

sealed class SearchStatus {
    data object None : SearchStatus()
    data class Wait(val query: String) : SearchStatus()
    data class Search(val query: String) : SearchStatus()
}