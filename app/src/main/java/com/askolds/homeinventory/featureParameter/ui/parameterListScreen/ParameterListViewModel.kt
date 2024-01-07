package com.askolds.homeinventory.featureParameter.ui.parameterListScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParameterListViewModel @Inject constructor(
    private val parameterUseCases: ParameterUseCases,
) : ViewModel() {
    var state by mutableStateOf(ParameterListState())
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

    fun onEvent(event: ParameterListEvent) {
        when (event) {
            is ParameterListEvent.DeleteSelected -> deleteSelected()
            is ParameterListEvent.QueryChanged -> search(event.query)
            is ParameterListEvent.SelectItem -> with (event) { selectItem(id, selected, index) }
            is ParameterListEvent.UnselectAll -> unselectAll()
        }
    }

    /**
     * Search thing list
     * @param query Query
     */
    private fun search(query: String) {
        state = state.copy(query = query)
        if (query.isNotBlank()) {
            // Cancel existing searches
            searchJob?.cancel()
            getListJob?.cancel()
            searchJob = viewModelScope.launch {
                delay(200) // don't search for 200ms so typing more cancels jobs
                parameterUseCases.search(query)
                    .onEach {
                        state = state.copy(parameterList = it.toMutableStateList())
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
        var item: ParameterListItem
        val itemIndex: Int
        if (index == null) {
            // find item and index if index not given
            item = state.parameterList.first { it.id == id }
            itemIndex = state.parameterList.indexOf(item)
        } else {
            // get the item, if index is given
            itemIndex = index
            item = state.parameterList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        state.parameterList[itemIndex] = item
        // update any selected
        state = state.copy(selectedCount =
            if (selected)
                state.selectedCount + 1
            else
                (state.selectedCount - 1).coerceAtLeast(0)
        )
    }

    private fun unselectAll() {
        state.parameterList.replaceAll { item -> item.copy(selected = false) }
        state = state.copy(selectedCount = 0)
    }

    private fun deleteSelected() {
        val ids = state.parameterList.filter { it.selected }.map {it.id}
        viewModelScope.launch {
            parameterUseCases.deleteList(ids)
        }

        state = state.copy(selectedCount = 0)
    }

    /**
     * Get list of things for current home/thing
     */
    private fun getList() {
        if (getListJob?.isActive != true) {
            searchJob?.cancel()
            getListJob = parameterUseCases.getListFlow()
                .onEach {
                    state = state.copy(parameterList = it.toMutableStateList())
                }
                .launchIn(viewModelScope)
        }
    }
}