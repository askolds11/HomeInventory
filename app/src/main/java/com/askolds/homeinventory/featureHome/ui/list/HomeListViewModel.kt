package com.askolds.homeinventory.featureHome.ui.list

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.model.HomeListItem
import com.askolds.homeinventory.featureHome.domain.usecase.home.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    var state by mutableStateOf(HomeListState())
        private set

    private var getListJob: Job? = null
    private var searchJob: Job? = null

    init {
        if (state.query.isBlank()) {
            state = state.copy(query = "")
            getList()
        } else {
            search(state.query)
        }
    }

    /**
     * Search home list
     * @param query Query
     */
    fun search(query: String) {
        state = state.copy(query = query)
        if (query.isNotBlank()) {
            // Cancel existing searches
            searchJob?.cancel()
            getListJob?.cancel()
            searchJob = homeUseCases.search(query)
                .onEach {
                    state = state.copy(homeList = it.toMutableStateList())
                }
                .launchIn(viewModelScope)
        } else {
            getList()
        }
    }

    /**
     * Select an item
     * @param id Id of selected item
     * @param selected Whether item is selected or not
     * @param index Index, if exists (for performance)
     */
    fun selectItem(id: Int, selected: Boolean, index: Int? = null) {
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
        state = state.copy(selectedCount =
             if (selected)
                state.selectedCount + 1
             else
                 (state.selectedCount - 1).coerceAtLeast(0)
        )
    }

    /**
     * Unselect all items
     */
    fun unselectAll() {
        state.homeList.replaceAll { item -> item.copy(selected = false) }
        state = state.copy(selectedCount = 0)
    }

    /**
     * Deletes selected items
     */
    fun deleteSelected() {
        val ids = state.homeList.filter { it.selected }.map {it.id}
        viewModelScope.launch {
            homeUseCases.deleteList(ids)
        }

        state = state.copy(selectedCount = 0)
    }

    /**
     * Get list of homes
     */
    private fun getList() {
        // Don't need to do anything, if job is already running
        if (getListJob?.isActive != true) {
            //cancel search if exists
            searchJob?.cancel()
            getListJob = homeUseCases.getListFlow()
                .onEach {
                    state = state.copy(homeList = it.toMutableStateList())
                }
                .launchIn(viewModelScope)
        }
    }
}