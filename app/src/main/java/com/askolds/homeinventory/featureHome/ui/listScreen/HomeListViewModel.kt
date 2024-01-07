package com.askolds.homeinventory.featureHome.ui.listScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.core.ui.SearchStatus
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.domain.usecase.home.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
class HomeListViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    var state = HomeListState()
        private set

    /**
     * Flow for current search status
     */
    private var searchStatusFlow: MutableStateFlow<SearchStatus> =
        MutableStateFlow(SearchStatus.None)

    /**
     * Flow that coordinates searching
     */
    @OptIn(ExperimentalCoroutinesApi::class)
    val getHomeListFlow: SharedFlow<Unit> = searchStatusFlow
        .flatMapLatest {
            return@flatMapLatest when (it) {
                // Search delay has expired, execute search
                is SearchStatus.Search -> searchFlow(it.query)
                // Query is empty, get all items
                is SearchStatus.None -> getHomeListFlow()
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
     * Flow that updates state.homeList based on search query state.query
     */
    private fun searchFlow(query: String): Flow<Unit> {
        return homeUseCases.search(query)
            .map {
                state.homeList.apply { clear() }.addAll(it)
                return@map
            }
    }

    /**
     * Flow that updates state.homeList
     */
    private fun getHomeListFlow(): Flow<Unit> {
        return homeUseCases.getListFlow()
            .map {
                state.homeList.apply { clear() }.addAll(it)
                return@map
            }
    }

    fun onEvent(event: HomeListEvent) {
        when (event) {
            is HomeListEvent.DeleteSelected -> deleteSelected()
            is HomeListEvent.QueryChanged -> search(event.query)
            is HomeListEvent.SelectItem -> with (event) { selectItem(id, selected, index) }
            is HomeListEvent.UnselectAll -> unselectAll()
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
     * @param index Index, if exists (for performance)
     */
    private fun selectItem(id: Int, selected: Boolean, index: Int? = null) {
        var item: HomeListItem
        val itemIndex: Int
        // find item and index if index not given
        if (index == null) {
            item = state.homeList.first { it.id == id }
            itemIndex = state.homeList.indexOf(item)
            // get the item, if index is given
        } else {
            itemIndex = index
            item = state.homeList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        state.homeList[itemIndex] = item
        // update any selected
        state.selectedCount.value =
             if (selected)
                state.selectedCount.value + 1
             else
                 (state.selectedCount.value - 1).coerceAtLeast(0)
    }

    /**
     * Unselect all items
     */
    private fun unselectAll() {
        state.homeList.replaceAll { item -> item.copy(selected = false) }
        state.selectedCount.value = 0
    }

    /**
     * Deletes selected items
     */
    private fun deleteSelected() {
        val ids = state.homeList.filter { it.selected }.map {it.id}
        viewModelScope.launch {
            homeUseCases.deleteList(ids)
        }

        state.selectedCount.value = 0
    }

    init {
        viewModelScope.launch {
            search(state.query.value, true)
            getHomeListFlow.first()
        }
    }
}