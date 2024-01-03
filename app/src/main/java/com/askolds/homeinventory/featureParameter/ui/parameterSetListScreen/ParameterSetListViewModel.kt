package com.askolds.homeinventory.featureParameter.ui.parameterSetListScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.ParameterSetUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParameterSetListViewModel @Inject constructor(
    private val parameterSetUseCases: ParameterSetUseCases,
    //savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ParameterSetListState())
        private set

    private var getListJob: Job? = null
    private var searchJob: Job? = null

    init {
        viewModelScope.launch {
            if (state.query.isBlank()) {
                state = state.copy(query = "")
                getList()
            } else {
                search(state.query)
            }
        }
    }

    fun onEvent(event: ParameterSetListEvent) {
        when (event) {
            is ParameterSetListEvent.DeleteSelected -> deleteSelected()
            is ParameterSetListEvent.QueryChanged -> search(event.query)
            is ParameterSetListEvent.SelectItem -> with (event) { selectItem(id, selected, index) }
            is ParameterSetListEvent.UnselectAll -> unselectAll()
        }
    }

    private fun search(query: String) {
        state = state.copy(query = query)
        if (query.isNotBlank()) {
            // Cancel existing searches
            searchJob?.cancel()
            getListJob?.cancel()

            searchJob = viewModelScope.launch {
                delay(200) // don't search for 200ms so typing more cancels jobs
                parameterSetUseCases.search(query)
                    .onEach {
                        state = state.copy(parameterSetList = it.toMutableStateList())
                    }
                    .launchIn(viewModelScope)
            }
        } else {
            getList()
        }
    }

    /**
     * Select an item
     * @param id Id of selected item
     * @param selected Whether item is selected or not
     * @param index index of item in list
     */
    private fun selectItem(id: Int, selected: Boolean, index: Int? = null) {
        var item: ParameterSetListItem
        val itemIndex: Int
        if (index == null) {
            // find item and index if index not given
            item = state.parameterSetList.first { it.id == id }
            itemIndex = state.parameterSetList.indexOf(item)
        } else {
            // get the item, if index is given
            itemIndex = index
            item = state.parameterSetList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        state.parameterSetList[itemIndex] = item
        // update any selected
        state = state.copy(selectedCount =
            if (selected)
                state.selectedCount + 1
            else
                (state.selectedCount - 1).coerceAtLeast(0)
        )
    }

    private fun unselectAll() {
        state.parameterSetList.replaceAll { item -> item.copy(selected = false) }
        state = state.copy(selectedCount = 0)
    }

    private fun deleteSelected() {
        val ids = state.parameterSetList.filter { it.selected }.map {it.id}
        viewModelScope.launch {
            parameterSetUseCases.deleteList(ids)
        }

        state = state.copy(selectedCount = 0)
    }

    private fun getList() {
        // No need to get list if already getting it
        if (getListJob?.isActive != true) {
            // Cancel existing search
            searchJob?.cancel()

            getListJob = parameterSetUseCases.getListFlow()
                .onEach {
                    state = state.copy(parameterSetList = it.toMutableStateList())
                }
                .launchIn(viewModelScope)
        }
    }
}