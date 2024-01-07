package com.askolds.homeinventory.featureThing.ui.list

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.core.ui.SearchStatus
import com.askolds.homeinventory.featureThing.domain.model.ThingListItem
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThingListViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state = ThingListState()
        private set
    private val homeId = savedStateHandle.get<Int>("homeId")!!
    private val thingId = savedStateHandle.get<Int>("thingId")
    private val isHomeScreen = thingId == null // how to get list - by home id or parent (thing) id

    /**
     * Flow for current search status
     */
    private var searchStatusFlow: MutableStateFlow<SearchStatus> =
        MutableStateFlow(SearchStatus.None)

    /**
     * Flow that updates state.isContainer, if the current screen is "Thing" and not "Home"
     */
    private val getThingFlow: SharedFlow<Unit> =
        if (thingId != null) {
            thingUseCases.getFlow(thingId)
                .map {
                    state.isContainer.value = it.isContainer
                }

        } else {
            flow { }
        }
            .shareIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000)
            )

    /**
     * Flow that coordinates searching
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val getThingListFlow: SharedFlow<Unit> = searchStatusFlow
        .flatMapLatest {
            return@flatMapLatest when (it) {
                // Search delay has expired, execute search
                is SearchStatus.Search -> searchFlow(it.query)
                // Query is empty, get all items
                is SearchStatus.None -> getThingListFlow()
                // Query has been updated - wait 200 ms before searching
                is SearchStatus.Wait -> {
                    delay(200)
                    searchStatusFlow.emit(SearchStatus.Search(it.query))
                    flow {}
                }
            }
        }
        .shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )

    /**
     * Flow that updates state.thingList based on search query state.query
     */
    private fun searchFlow(query: String): Flow<Unit> {
        return thingUseCases.search(homeId, thingId, query)
            .map {
                state.thingList.apply { clear() }.addAll(it)
                return@map
            }
    }

    /**
     * Flow that updates state.thingList with all things in the current home/thing
     */
    private fun getThingListFlow(): Flow<Unit> {
        return thingUseCases.getListFlow(homeId, thingId)
            .map {
                state.thingList.apply { clear() }.addAll(it)
                return@map
            }
    }

    private fun getState(): Deferred<Unit> {
        state.homeId.value = homeId
        state.thingId.value = thingId
        return viewModelScope.async {
            return@async if (isHomeScreen) {
                state.isContainer.value = true
            } else {
                getThingFlow.first()
                return@async
            }
        }
    }

    fun onEvent(event: ThingListEvent) {
        when (event) {
            is ThingListEvent.DeleteSelected -> deleteSelected()
            is ThingListEvent.QueryChanged -> search(event.query)
            is ThingListEvent.SelectItem -> with(event) { selectItem(id, selected, index) }
            is ThingListEvent.UnselectAll -> unselectAll()
        }
    }

    /**
     * @param query query
     * @param force execute search immediately
     */
    private fun search(query: String, force: Boolean = false) {
        state.query.value = query
        viewModelScope.launch {
            searchStatusFlow.emit(
                if (query.isNotBlank()) {
                    if (force)
                        SearchStatus.Search(query)
                    else
                        SearchStatus.Wait(query)
                } else {
                    SearchStatus.None
                }
            )
        }
    }

    /**
     * Select an item
     * @param id Id of selected item
     * @param selected Whether item is selected or not
     * @param index index of item in list
     */
    private fun selectItem(id: Int, selected: Boolean, index: Int? = null) {
        var item: ThingListItem
        val itemIndex: Int
        if (index == null) {
            // find item and index if index not given
            item = state.thingList.first { it.id == id }
            itemIndex = state.thingList.indexOf(item)
        } else {
            // get the item, if index is given
            itemIndex = index
            item = state.thingList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        state.thingList[itemIndex] = item
        // update any selected
        state.selectedCount.value = if (selected)
            state.selectedCount.value + 1
        else
            (state.selectedCount.value - 1).coerceAtLeast(0)
    }

    private fun unselectAll() {
        state.thingList.replaceAll { item -> item.copy(selected = false) }
        state.selectedCount.value = 0
    }

    private fun deleteSelected() {
        val ids = state.thingList.filter { it.selected }.map { it.id }
        viewModelScope.launch {
            thingUseCases.deleteList(ids)
        }

        state.selectedCount.value = 0
    }

    init {
        viewModelScope.launch {
            getState().await()
            search(state.query.value, true)
            getThingListFlow.first()
        }
    }
}