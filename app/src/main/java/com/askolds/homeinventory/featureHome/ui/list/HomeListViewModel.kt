package com.askolds.homeinventory.featureHome.ui.list

import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

@HiltViewModel
class HomeListViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    private val _state = mutableStateOf(HomeListState())
    val state: State<HomeListState> = _state

    private var getListJob: Job? = null
    private var searchJob: Job? = null

    var query by mutableStateOf("")
        private set

    init {
        if (query.isBlank()) {
            query = ""
            getList()
        } else {
            search(query)
        }
    }
    fun search(query: String) {
        this.query = query
        if (query.isNotBlank()) {
            searchJob?.cancel()
            getListJob?.cancel()
            searchJob = homeUseCases.search(query)
                .onEach {
                    _state.value = state.value.copy(homeList = it)
                }
                .launchIn(viewModelScope)
        } else {
            getList()
        }
        _state.value = state.value.copy(
            homeList = state.value.homeList.filter { it.name.contains(query.lowercase()) }
        )
    }

    private fun getList() {
        if (getListJob?.isActive != true) {
            searchJob?.cancel()
            getListJob = homeUseCases.getList()
                .onEach {
                    _state.value = state.value.copy(homeList = it)
                }
                .launchIn(viewModelScope)
        }
    }
}