package com.askolds.homeinventory.featureParameter.ui.parameterFormScreen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.model.Parameter
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import com.askolds.homeinventory.core.ui.SaveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ParameterFormViewModel @Inject constructor(
    private val parameterUseCases: ParameterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ParameterFormState())
        private set

    private val parameterId = savedStateHandle.get<Int?>("parameterId")

    private var originalParameter: Parameter? = null

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    // only for create - name will not be validated if it is not the first thing to change
    private var nameValidated: Boolean = false

    init {
        viewModelScope.launch {
            getParameter().await()
        }
    }

    private fun getParameter(): Deferred<Unit> {
        return viewModelScope.async {
            return@async if (parameterId == null) {
                // create
                state = state.copy (
                    isEditMode = false
                )
            } else {
                // edit
                state = state.copy (
                    isEditMode = true
                )
                val parameter = parameterUseCases.get(parameterId)!! // is it ok for it to be non null?
                originalParameter = parameter
                state = state.copy (
                    parameter = parameter
                )
                nameValidated = true
            }
        }
    }

    // region Events
    fun onEvent(event: ParameterFormEvent) {
        when (event) {
            is ParameterFormEvent.NameChanged -> nameChanged(event)
            is ParameterFormEvent.Submit -> submit()
        }
    }

    private fun nameChanged(event: ParameterFormEvent.NameChanged) {
        saveEnabled = false
        state = state.copy(parameter = state.parameter.copy(name = event.name))
        validateName()
    }

    private fun submit() {
        validateName()
        if (saveEnabled) {
            state = state.copy(
                saveEnabled = false,
                saveStatus = SaveStatus.Saving
            )
            saveEnabled = false
            viewModelScope.launch {
                val parameter = state.parameter.copy(
                    name = state.parameter.name.trim()
                )
                if (parameterId == null) {
                    //create
                    parameterUseCases.add(parameter)
                } else {
                    //update
                    parameterUseCases.update(parameter)
                }

                state = state.copy(saveStatus = SaveStatus.Saved)
                saveEnabled = true
            }
        }
    }
    // endregion Events

    private fun validateName() {
        val name = state.parameter.name
        viewModelScope.launch {
            nameValidated = true
            // do not need to validate, if name is same as original (edit)
            val localParameter = originalParameter
            if (localParameter != null && name.trim() == localParameter.name.trim()) {
                state = state.copy(nameValidation = null)
                isValid()
                return@launch
            }
            // the name is different from original (edit/create)
            // !! all validations are on trimmed string, also saved as trimmed string
            val validation = parameterUseCases.validateName(name, null)
            state = state.copy(nameValidation = validation)
            isValid()
        }
    }

    // Check if all validations have passed
    // If not, disable saving
    // Else, enable saving
    private fun isValid(): Boolean {
        var isValid = true
        if (!nameValidated) {
            isValid = false
        } else {
            with (state) {
                val validations = arrayOf(nameValidation)
                if (validations.any {it != null}) {
                    isValid = false
                }
            }
        }

        if (state.saveEnabled != isValid) {
            state = state.copy(saveEnabled = isValid)
        }
        saveEnabled = isValid
        return isValid
    }
}