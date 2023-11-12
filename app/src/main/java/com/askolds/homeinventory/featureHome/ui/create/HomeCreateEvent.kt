package com.askolds.homeinventory.featureHome.ui.create

sealed class HomeCreateEvent {
    data class NameChanged(val name: String): HomeCreateEvent()
    data object Submit: HomeCreateEvent()
}