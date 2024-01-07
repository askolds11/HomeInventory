package com.askolds.homeinventory.featureParameter.ui.parameterSetForm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSet
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.ParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.ParameterSetParameterUseCases
import com.askolds.homeinventory.core.ui.SaveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


/**
 * Form for creating or editing a parameter set
 * if editing, then parameterSetId in route is not null
 */
@HiltViewModel
class ParameterSetFormViewModel @Inject constructor(
    private val parameterSetParameterUseCases: ParameterSetParameterUseCases,
    private val parameterSetUseCases: ParameterSetUseCases,
    private val parameterUseCases: ParameterUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ParameterSetFormState())
        private set

    private val parameterSetId = savedStateHandle.get<Int?>("parameterSetId")

    private var originalParameterSet: ParameterSet? = null

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    // only for create
    // if editing then the name will have already been validated (true)
    private var nameValidated: Boolean = false

    init {
        viewModelScope.launch {
            getParameterSet()
            getParameters()
        }
    }

    private fun getParameterSet() {
        viewModelScope.launch {
            if (parameterSetId == null) {
                // create
                state = state.copy (
                    isEditMode = false
                )
            } else {
                // edit
                state = state.copy (
                    isEditMode = true
                )
                val parameterSet = parameterSetUseCases.get(parameterSetId)!! // is it ok for it to be non null?
                originalParameterSet = parameterSet
                state = state.copy (
                    parameterSet = parameterSet
                )
                nameValidated = true
            }
        }
    }

    private fun getParameters() {
        viewModelScope.launch {
            state = if (parameterSetId == null) {
                // create
                val parameters = parameterUseCases.getList()
                state.copy(parameterList = parameters.toMutableStateList())
            } else {
                val parameters = parameterSetParameterUseCases.getAllParameterList(parameterSetId)
                state.copy(parameterList = parameters.toMutableStateList())
            }
        }
    }

    // region Events
    fun onEvent(event: ParameterSetFormEvent) {
        when (event) {
            is ParameterSetFormEvent.NameChanged -> nameChanged(event)
            is ParameterSetFormEvent.SelectItem -> selectItem(event.id, event.selected, event.index)
            is ParameterSetFormEvent.UnselectAll -> unselectAll()
            is ParameterSetFormEvent.Submit -> submit()
        }
    }

    private fun nameChanged(event: ParameterSetFormEvent.NameChanged) {
        saveEnabled = false
        state = state.copy(parameterSet = state.parameterSet.copy(name = event.name))
        validateName()
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
    }

    private fun unselectAll() {
        state.parameterList.replaceAll { item -> item.copy(selected = false) }
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
                val parameterSet = state.parameterSet.copy(
                    name = state.parameterSet.name.trim()
                )
                val selectedParameterIds = state.parameterList.filter { it.selected }.map { it.id }
                if (parameterSetId == null) {
                    //create
                    parameterSetUseCases.add(parameterSet, selectedParameterIds)
                } else {
                    //update
                    parameterSetUseCases.update(parameterSet, selectedParameterIds)
                }

                delay(2000)
                state = state.copy(saveStatus = SaveStatus.Saved)
                saveEnabled = true
            }
        }
    }
    // endregion Events

    private fun validateName() {
        val name = state.parameterSet.name
        viewModelScope.launch {
            nameValidated = true
            // do not need to validate, if name is same as original (edit)
            val localParameterSet = originalParameterSet
            if (localParameterSet != null && name.trim() == localParameterSet.name.trim()) {
                state = state.copy(nameValidation = null)
                isValid()
                return@launch
            }
            // the name is different from original (edit/create)
            // !! all validations are on trimmed string, also saved as trimmed string
            val validation = parameterSetUseCases.validateName(name, localParameterSet?.name)
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