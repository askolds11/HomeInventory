package com.askolds.homeinventory.featureHome.ui.create

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeCreateViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases
) : ViewModel() {
    private val _state = mutableStateOf(HomeCreateState())
    val state: State<HomeCreateState> = _state

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    fun onEvent(event: HomeCreateEvent) {
        when (event) {
            is HomeCreateEvent.NameChanged -> nameChanged(event)
            is HomeCreateEvent.Submit -> submit(event)
        }
    }

    fun resetState() {
        _state.value = HomeCreateState()
    }

    private fun nameChanged(event: HomeCreateEvent.NameChanged) {
        _state.value = _state.value.copy(name = event.name)
        saveEnabled = false
        viewModelScope.launch {
            val validation = homeUseCases.validateName(event.name, null)
            _state.value = _state.value.copy(nameValidation = validation)
            isValid()
        }
    }

    private fun submit(event: HomeCreateEvent.Submit) {
        _state.value = _state.value.copy(saveEnabled = false, saveStatus = SaveStatus.Saving)
        saveEnabled = false

        viewModelScope.launch {
            homeUseCases.add(_state.value.name.trim())
            //delay(2000)
            _state.value = _state.value.copy(saveStatus = SaveStatus.Saved)
        }
    }

    private fun isValid(): Boolean {
        var isValid = true
        with (_state.value) {
            val validations = arrayOf(nameValidation)
            if (validations.any {it != null}) {
                isValid = false
            }
        }

        if (_state.value.saveEnabled != isValid) {
            _state.value = _state.value.copy(saveEnabled = isValid)
        }
        saveEnabled = isValid
        return isValid
    }
}