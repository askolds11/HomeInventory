package com.askolds.homeinventory.featureHome.ui.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureHome.domain.model.Home
import com.askolds.homeinventory.featureHome.domain.usecase.HomeUseCases
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.domain.usecase.ImageUseCases
import com.askolds.homeinventory.ui.SaveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeFormViewModel @Inject constructor(
    private val homeUseCases: HomeUseCases,
    private val imageUseCases: ImageUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(HomeFormState())
        private set

    private val homeId = savedStateHandle.get<Int?>("homeId")

    private var originalHome: Home? = null

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    // only for create - name will not be validated if it is not the first thing to change
    private var nameValidated: Boolean = false

    init {
        viewModelScope.launch {
            getHome().await()
        }
    }

    private fun getHome(): Deferred<Unit> {
        return viewModelScope.async {
            return@async if (homeId == null) {
                // create
                Unit // do nothing
            } else {
                // edit
                val home = homeUseCases.get(homeId)!! // is it ok for it to be non null?
                originalHome = home
                state = state.copy (
                    home = home
                )
                nameValidated = true
            }
        }
    }

    // region Events
    fun onEvent(event: HomeFormEvent) {
        when (event) {
            is HomeFormEvent.NameChanged -> nameChanged(event)
            is HomeFormEvent.ImageChanged -> imageChanged(event)
            is HomeFormEvent.Submit -> submit()
        }
    }

    private fun nameChanged(event: HomeFormEvent.NameChanged) {
        saveEnabled = false
        state = state.copy(home = state.home.copy(name = event.name))
        validateName()
    }

    private fun imageChanged(event: HomeFormEvent.ImageChanged) {
        viewModelScope.launch {
            val image =
                // deleted
                if (event.imageUri == null)
                    null
                // added / changed
                else {
                    // compress now, instead of only on save
                    val imageUri = imageUseCases.compress(event.imageUri)
                    val fileName = imageUseCases.getFileName(event.imageUri)
                    Image(
                        fileName = fileName ?: "",
                        imageUri = imageUri.toString()
                    )
                }

            state = state.copy(
                home = state.home.copy(
                    image = image
                )
            )
            isValid()
        }
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
                val home = state.home.copy(
                    name = state.home.name.trim()
                )
                if (homeId == null) {
                    //create
                    homeUseCases.add(home)
                } else {
                    //update
                    homeUseCases.update(home)
                }

                delay(2000)
                state = state.copy(saveStatus = SaveStatus.Saved)
                saveEnabled = true
            }
        }
    }
    // endregion Events

    private fun validateName() {
        val name = state.home.name
        viewModelScope.launch {
            nameValidated = true
            // do not need to validate, if name is same as original (edit)
            val localHome = originalHome
            if (localHome != null && name.trim() == localHome.name.trim()) {
                state = state.copy(nameValidation = null)
                isValid()
                return@launch
            }
            // the name is different from original (edit/create)
            // !! all validations are on trimmed string, also saved as trimmed string
            val validation = homeUseCases.validateName(name, null)
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