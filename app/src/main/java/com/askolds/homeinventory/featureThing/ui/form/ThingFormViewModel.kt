package com.askolds.homeinventory.featureThing.ui.form

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.domain.usecase.ImageUseCases
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.usecase.ThingUseCases
import com.askolds.homeinventory.ui.SaveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThingFormViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    private val imageUseCases: ImageUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    var state by mutableStateOf(ThingFormState())
        private set

    private val homeId = savedStateHandle.get<Int?>("homeId") // only for create
    private val parentId = savedStateHandle.get<String?>("parentId")?.toInt() // only for create
    private val thingId = savedStateHandle.get<Int?>("thingId") // only for edit

    private var originalThing: Thing? = null

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    // only for create - name will not be validated if it is not the first thing to change
    private var nameValidated: Boolean = false

    init {
        viewModelScope.launch {
            getThing().await()
        }
    }

    private fun getThing(): Deferred<Unit> {
        return viewModelScope.async {
            return@async if (thingId == null) {
                // create
                state = state.copy(
                    thing = state.thing.copy(
                        homeId = homeId!!,
                        parentId = parentId
                    )
                )
            } else {
                // edit
                val thing = thingUseCases.get(thingId)!! // is it ok for it to be non null?
                val image =
                    if (thing.imageId != null)
                        imageUseCases.get(thing.imageId)
                    else
                        null
                originalThing = thing
                state = state.copy(
                    thing = thing,
                    image = image,
                )
            }
        }
    }

    fun onEvent(event: ThingFormEvent) {
        when (event) {
            is ThingFormEvent.NameChanged -> nameChanged(event)
            is ThingFormEvent.IsContainerChanged -> isContainerChanged(event)
            is ThingFormEvent.ImageChanged -> imageChanged(event)
            is ThingFormEvent.Submit -> submit()
        }
    }

    private fun nameChanged(event: ThingFormEvent.NameChanged) {
        saveEnabled = false
        state = state.copy(
            thing = state.thing.copy(
                name = event.name
            )
        )
        validateName()
    }

    private fun isContainerChanged(event: ThingFormEvent.IsContainerChanged) {
        saveEnabled = false
        state = state.copy(
            thing = state.thing.copy(
                isContainer = event.isContainer
            )
        )
        isValid()
    }

    private fun imageChanged(event: ThingFormEvent.ImageChanged) {
        viewModelScope.launch {
            val image =
                // deleted
                if (event.imageUri == null)
                    null
                // added / changed
                else {
                    val imageUri = imageUseCases.compress(event.imageUri)
                    val fileName = imageUseCases.getFileName(event.imageUri)
                    Image(
                        fileName = fileName ?: "",
                        imageUri = imageUri.toString()
                    )
                }

            state = state.copy(
                image = image
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
                val thing = with(state.thing) {
                    copy(
                        name = name.trim()
                    )
                }
                if (thingId == null) {
                    //create
                    thingUseCases.add(thing, state.image)
                } else {
                    //update
                    thingUseCases.update(thing, state.image)
                }
                delay(2000)
                state = state.copy(saveStatus = SaveStatus.Saved)
            }
        }
    }


    private fun validateName() {
        val name = state.thing.name
        viewModelScope.launch {
            nameValidated = true
            // do not need to validate, if name is same as original (edit)
            val localThing = originalThing
            if (localThing != null && name.trim() == localThing.name.trim()) {
                state = state.copy(nameValidation = null)
                isValid()
                return@launch
            }
            // the name is different from original (edit/create)
            // !! all validations are on trimmed string, also saved as trimmed string
            val validation = thingUseCases.validateName(name, null)
            state = state.copy(nameValidation = validation)
            isValid()
        }
    }

    /**
     * Check if all validations have passed
     * If not, disable saving
     * Else, enable saving
     */
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