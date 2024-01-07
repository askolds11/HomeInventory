package com.askolds.homeinventory.featureImageNavigation.ui.imageNavOverlay

import android.util.Log
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.core.ui.SaveStatus
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.model.ImageThingNavigation
import com.askolds.homeinventory.featureImageNavigation.domain.imageThingNavigation.usecase.ImageThingNavigationUseCases
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ImageNavOverlayViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    private val imageThingNavigationUseCases: ImageThingNavigationUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state = ImageNavOverlayState()
        private set

    private val thingId = savedStateHandle.get<Int>("thingId")!!
    private val homeId = savedStateHandle.get<Int>("homeId")!!

    init {
        viewModelScope.launch {
            val thing = thingUseCases.getFlow(thingId).first()
            state.homeId.intValue = thing.homeId
            getListFlow.first()
        }
    }

    val getListFlow: SharedFlow<Unit> = imageThingNavigationUseCases.getListByThingId(thingId)
        .map {
            state.imageThingNavigationList.apply { clear() }.addAll(it)
            return@map
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        )

    val getThingsListFlow: SharedFlow<Unit> = thingUseCases
        .getListFlow(homeId, thingId)
        .map {
            state.thingList.apply { clear() }.addAll(it)
            return@map
    }.shareIn(
        viewModelScope,
        SharingStarted.WhileSubscribed()
    )

    // region Events
    fun onEvent(event: ImageNavOverlayEvent) {
        when (event) {
            is ImageNavOverlayEvent.AddItem -> addItem(event.offset, event.size)
            ImageNavOverlayEvent.DecreaseZIndex -> decreaseZIndex()
            ImageNavOverlayEvent.IncreaseZIndex -> increaseZIndex()
            ImageNavOverlayEvent.RemoveItem -> removeItem()
            is ImageNavOverlayEvent.SelectItem -> selectItem(event.position)
            is ImageNavOverlayEvent.Submit -> submit()
            is ImageNavOverlayEvent.ClearState -> clearState(event.editMode)
            is ImageNavOverlayEvent.SelectThing -> selectThing(event.thingId)
        }
    }

    private fun addItem(offset: Offset, newSize: Size) {
        with(state.imageThingNavigationList) {
            // Unselect all items
            for (index in indices) {
                set(index, get(index).copy(selected = false))
            }

            // Add new item to list and select it
            add(
                ImageThingNavigation(
                    thingId = null,
                    offsetX = offset.x,
                    offsetY = offset.y,
                    sizeX = newSize.width,
                    sizeY = newSize.height,
                    zIndex = size,
                    selected = true
                )
            )
            state.selectedIndex.value = size - 1
        }
    }

    private fun increaseZIndex() {
        val index = state.selectedIndex.value
        if (index != null) {
            with(state.imageThingNavigationList) {
                set(
                    index,
                    get(index)
                        .copy(zIndex = index + 1)
                )
                set(
                    index + 1,
                    get(index + 1)
                        .copy(zIndex = index)
                )
                set(index, get(index + 1).also { set(index + 1, get(index)) })
            }
            state.selectedIndex.value = index + 1
        }
    }

    private fun decreaseZIndex() {
        val index = state.selectedIndex.value
        if (index != null) {
            with(state.imageThingNavigationList) {
                set(
                    index,
                    get(index)
                        .copy(zIndex = get(index).zIndex - 1)
                )
                set(
                    index - 1,
                    get(index - 1)
                        .copy(zIndex = get(index - 1).zIndex + 1)
                )
                set(index, get(index - 1).also { set(index - 1, get(index)) })
            }
            state.selectedIndex.value = index - 1
        }
    }

    private fun removeItem() {
        state.imageThingNavigationList.removeAt(state.selectedIndex.value!!)
        for (index in state.imageThingNavigationList.indices) {
            if (state.imageThingNavigationList[index].zIndex != index) {
                with (state.imageThingNavigationList) {
                    set(index, get(index).copy(zIndex = index))
                }
            }
        }
        state.selectedIndex.value = 0
    }

    private fun selectItem(position: Offset) {
        // Find which items were clicked on
        val selectedItem = state.imageThingNavigationList.filter {
            val x1 = if (it.sizeX > 0) it.offsetX else it.offsetX + it.sizeX
            val x2 = if (it.sizeX > 0) it.offsetX + it.sizeX else it.offsetX
            val y1 = if (it.sizeY > 0) it.offsetY else it.offsetY + it.sizeY
            val y2 = if (it.sizeY > 0) it.offsetY + it.sizeY else it.offsetY

            return@filter position.x in (x1..x2)
                    && position.y in (y1..y2)
            // Find top item
        }.maxByOrNull {
            it.zIndex
        }
        Log.d("Test", position.toString())
        Log.d("Test", selectedItem.toString())
        if (selectedItem != null) {
            val selectedIndex = state.imageThingNavigationList.indexOf(selectedItem)

            with(state.imageThingNavigationList) {
                for (index in indices) {
                    set(
                        index, get(index).copy(
                            selected = index == selectedIndex
                        )
                    )
                }

                state.selectedIndex.value = selectedIndex
                state.navigate.value = get(selectedIndex).thingId != null
            }
        } else {
            with(state.imageThingNavigationList) {
                for (index in indices) {
                    set(index, get(index).copy(selected = false))
                }
            }
            state.selectedIndex.value = null
        }
        getThingName()
    }

    private fun submit() {
        state.saveStatus.value = SaveStatus.Saving

        viewModelScope.launch {
//            val home = state.home.value.copy(
//                name = state.home.value.name.trim()
//            )
            imageThingNavigationUseCases.changeImageThingNavigations(
                thingId, state.imageThingNavigationList
            )
            state.saveStatus.value = SaveStatus.Saved
        }
    }

    private fun clearState(editMode: Boolean) {
        state.saveStatus.value = SaveStatus.None
        state.navigate.value = false
    }

    private fun getThingName() {
        viewModelScope.launch {
            val selectedIndex = state.selectedIndex.value
            if (selectedIndex != null) {
                val thingId = state.imageThingNavigationList[selectedIndex].thingId
                if (thingId != null) {
                    val thing = thingUseCases.getFlow(thingId).first()
                    state.thingName.value = thing.name
                } else {
                    state.thingName.value = ""
                }

            }
        }

    }

    // endregion Events

    //region Things selection
    private fun selectThing(thingId: Int) {
        val selectedIndex = state.selectedIndex.value
        if (selectedIndex != null) {
            state.imageThingNavigationList[selectedIndex] = state.imageThingNavigationList[selectedIndex].copy (
                thingId = thingId
            )
        }
        getThingName()
    }
    //endregion Things selection
}