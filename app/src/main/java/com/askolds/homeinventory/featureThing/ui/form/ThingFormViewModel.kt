package com.askolds.homeinventory.featureThing.ui.form

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.askolds.homeinventory.featureImage.domain.model.Image
import com.askolds.homeinventory.featureImage.domain.usecase.ImageUseCases
import com.askolds.homeinventory.featureParameter.domain.model.ParameterListItem
import com.askolds.homeinventory.featureParameter.domain.model.ParameterSetListItem
import com.askolds.homeinventory.featureParameter.domain.model.ThingParameter
import com.askolds.homeinventory.featureParameter.domain.usecase.parameter.ParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.ParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.parameterSetParameter.ParameterSetParameterUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterParameterSet.ThingParameterParameterSetUseCases
import com.askolds.homeinventory.featureParameter.domain.usecase.thingParameterSet.ThingParameterSetUseCases
import com.askolds.homeinventory.featureThing.domain.model.Thing
import com.askolds.homeinventory.featureThing.domain.usecase.thing.ThingUseCases
import com.askolds.homeinventory.featureThing.ui.form.parameterSets.ThingParameterSetsEvent
import com.askolds.homeinventory.featureThing.ui.form.parameterSets.ThingParameterSetsState
import com.askolds.homeinventory.featureThing.ui.form.parameters.ThingParametersEvent
import com.askolds.homeinventory.featureThing.ui.form.parameters.ThingParametersState
import com.askolds.homeinventory.ui.SaveStatus
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ThingFormViewModel @Inject constructor(
    private val thingUseCases: ThingUseCases,
    private val imageUseCases: ImageUseCases,
    private val parameterUseCases: ParameterUseCases,
    private val parameterSetUseCases: ParameterSetUseCases,
    private val parameterSetParameterUseCases: ParameterSetParameterUseCases,
    private val thingParameterSetUseCases: ThingParameterSetUseCases,
    private val thingParameterParameterSetUseCases: ThingParameterParameterSetUseCases,
    savedStateHandle: SavedStateHandle
) : ViewModel() {
    // region All screens
    /**
     * Flow for selected parameters' ids and names
     */
    private val selectedParametersIdNameFlow: MutableStateFlow<List<ParameterIdName>> =
        MutableStateFlow(emptyList())
    private val selectedParametersIdName: List<ParameterIdName>
        get() = selectedParametersIdNameFlow.value

    /**
     * Flow that gets and updates latest parameters
     * and selected parameter names
     */
    val parametersListFlow: SharedFlow<Unit> = parameterUseCases.getListFlow()
        .map { items ->
            // update parameters list
            parametersState = parametersState.copy(
                parameterList = items
                    .parametersSelectSelected()
                    .toMutableStateList()
            )

            // update selected params
            items
                .filter {item -> selectedParametersIdName.any {item.id == it.id}}
                .forEach {newParameter ->
                    // SnapshotStateList.forEachIndexed throws java.util.ConcurrentModificationException!
                    for(index in state.thingParameters.indices) {
                        val thingParameter = state.thingParameters[index]
                        if (newParameter.id != thingParameter.parameterId)
                            return@forEach

                        if (thingParameter.parameterName != newParameter.name) {
                            state.thingParameters[index] = thingParameter.copy(parameterName = newParameter.name)
                        }
                }
            }
            //return@map Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )


    /**
     * Flow for selected parameter sets' ids and names
     */
    private val selectedParameterSetsIdNameFlow: MutableStateFlow<List<ParameterSetIdName>> =
        MutableStateFlow(emptyList())
    private val selectedParameterSetsIdName: List<ParameterSetIdName>
        get() = selectedParameterSetsIdNameFlow.value

    /**
     * Flow that gets and updates latest parameter sets
     * and selected parameter sets' names
     */
    val parameterSetsListFlow: SharedFlow<Unit> = parameterSetUseCases.getListFlow()
        .map { items ->
            // update parameter sets list
            parameterSetsState = parameterSetsState.copy(
                parameterSetList = items
                    .parameterSetsSelectSelected()
                    .toMutableStateList()
            )

            // update selected parameter sets
            items
                // remove non selected sets
                .filter {item -> selectedParameterSetsIdName.any {item.id == it.id}}
                .forEach {newParameterSet ->
                    // SnapshotStateList.forEachIndexed throws java.util.ConcurrentModificationException!
                    // find the corresponding selected parameter set
                    for(index in state.thingParameterSets.indices) {
                        val thingParameterSet = state.thingParameterSets[index]
                        if (newParameterSet.id != thingParameterSet.parameterSetId)
                            return@forEach

                        //if names mismatch, change the name
                        if (thingParameterSet.parameterSetName != newParameterSet.name) {
                            state.thingParameterSets[index] = thingParameterSet
                                .copy(parameterSetName = newParameterSet.name)
                        }
                    }
                }
//            return@map Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000)
        )

    // endregion All screens

    // region Form
    // region Form variables
    var state by mutableStateOf(ThingFormState())
        private set


    private val homeId = savedStateHandle.get<Int?>("homeId") // only for create
    private val parentId = savedStateHandle.get<String?>("parentId")?.toInt() // only for create
    private val thingId = savedStateHandle.get<Int?>("thingId") // only for edit

    private val isEditMode = thingId != null
    private var originalThing: Thing? = null

    // to prevent the save button from doing anything
    // if validation hasn't finished yet
    // but to prevent the save button blinking
    private var saveEnabled: Boolean = false

    // only for create - name will not be validated if it is not the first thing to change
    private var nameValidated: Boolean = false

    // endregion Form variables
    /**
     * Flow that updates selected parameters list
     */
    val parametersValuesListFlow: SharedFlow<Unit> = selectedParametersIdNameFlow
        .map { items ->
            val selectedParameters: MutableList<ParameterIdName> = items.toMutableList()
            val thingParameters = state.thingParameters.toMutableList()
            // Remove removed parameters
            thingParameters.removeAll {thingParameter ->
                selectedParameters.none { selectedParameter ->
                    selectedParameter.id == thingParameter.parameterId
                }
            }
            // Don't do anything to the already existing parameters
            selectedParameters.removeAll {
                thingParameters
                    .any { thingParameter -> thingParameter.parameterId == it.id }
            }
            // Add new parameters
            thingParameters.addAll(
                selectedParameters.map { parameter ->
                    ThingParameter(
                        id = 0,
                        parameterId = parameter.id,
                        parameterSetId = null,
                        parameterSetParameterId = null,
                        thingParameterSetId = null,
                        value = "",
                        parameterName = parameter.name
                    )
                }
            )
            state = state.copy(
                thingParameters = thingParameters
                    .sortedBy { it.parameterName.lowercase() }
                    .sortedBy { it.parameterName }
                    .toMutableStateList()
            )
//            return@map Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        )

    /**
     * Flow that updates selected parameter sets list
     */
    val parameterSetsValuesListFlow: SharedFlow<Unit> = selectedParameterSetsIdNameFlow
        .map { items ->
            val selectedParameterSets: MutableList<ParameterSetIdName> = items.toMutableList()
            val thingParameterSets = state.thingParameterSets.toMutableList()
            // Remove removed parameter sets
            thingParameterSets.removeAll {
                selectedParameterSets.none { selectedParameterSet ->
                    selectedParameterSet.id == it.parameterSetId
                }
            }
            // Don't do anything to the already existing parameters
            selectedParameterSets.removeAll {
                thingParameterSets
                    .any { thingParameterSet -> thingParameterSet.parameterSetId == it.id }
            }
            // Add new parameters
            thingParameterSets.addAll(
                selectedParameterSets.map { parameterSet ->
                    SnapshotThingParameterSet(
                        parameterSetId = parameterSet.id,
                        parameterSetName = parameterSet.name,
                        thingParameters = parameterSetParameterUseCases
                            .getFlowParameterList(parameterSet.id)
                            .first()
                            .map {
                                ThingParameter(
                                    id = 0,
                                    value = "",
                                    parameterId = it.id,
                                    parameterSetId = parameterSet.id,
                                    parameterSetParameterId = 0, // TODO: Get this here and remove from save function
                                    thingParameterSetId = 0,
                                    parameterName = it.name,
                                )
                            }
                            .toMutableStateList()
                    )
                }
            )
            state = state.copy(
                thingParameterSets = thingParameterSets
                    .sortedBy { it.parameterSetName.lowercase() }
                    .sortedBy { it.parameterSetName }
                    .toMutableStateList()
            )
//            return@map Unit
        }.shareIn(
            viewModelScope,
            SharingStarted.WhileSubscribed()
        )

    // region Form init
    private fun formInit() {
        getThing()
        getThingParameters()
        getThingParameterSets()
    }

    /**
     * initial load for thing data
     */
    private fun getThing() {
        viewModelScope.launch {
            if (!isEditMode) {
                // create
                state = state.copy(
                    isEditMode = false,
                    thing = state.thing.copy(
                        homeId = homeId!!,
                        parentId = parentId
                    )
                )
            } else {
                // edit
                val thing = thingUseCases.get(thingId!!)!! // is it ok for it to be non null?
                originalThing = thing
                state = state.copy(
                    isEditMode = true,
                    thing = thing,
                )
                nameValidated = true

                val image =
                    if (thing.imageId != null)
                        imageUseCases.get(thing.imageId)
                    else
                        null
                state = state.copy(
                    image = image,
                )
            }
        }
    }

    /**
     * initial load for thing parameters data
     */
    private fun getThingParameters() {
        if (isEditMode) {
            viewModelScope.launch {
                val thingParameters = thingParameterParameterSetUseCases
                    .getListWithoutSetByThingId(thingId!!)
                    .first()
                state = state.copy(
                    thingParameters = thingParameters.toMutableStateList()
                )
                selectedParametersIdNameFlow.emit(
                    thingParameters.map {
                        ParameterIdName(
                            it.parameterId,
                            it.parameterName
                        )
                    }
                )
            }
        }
    }

    /**
     * initial load for thing parameter sets data
     */
    private fun getThingParameterSets() {
        if (isEditMode) {
            viewModelScope.launch {
                val thingParameterSets = thingParameterParameterSetUseCases
                    .getListGroupedBySetByThingId(thingId!!)
                    .first()
                Log.d("Test", thingParameterSets.joinToString(", "))
                state = state.copy(
                    thingParameterSets = thingParameterSets
                        .map {
                            it.toSnapshotThingParameterSet()
                        }
                        .toMutableStateList()
                )
                selectedParameterSetsIdNameFlow.emit(
                    thingParameterSets.map {
                        ParameterSetIdName(
                            it.parameterSetId,
                            it.parameterSetName
                        )
                    }
                )
            }
        }
    }

    // endregion Form init

    // region Form events
    fun onEvent(event: ThingFormEvent) {
        when (event) {
            is ThingFormEvent.NameChanged -> nameChanged(event)
            is ThingFormEvent.IsContainerChanged -> isContainerChanged(event)
            is ThingFormEvent.ImageChanged -> imageChanged(event)
            is ThingFormEvent.ParameterValueChanged -> parameterValueChanged(event)
            is ThingFormEvent.ParameterSetParameterValueChanged -> parameterSetParameterValueChanged(event)
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

    private fun parameterValueChanged(event: ThingFormEvent.ParameterValueChanged) {
        var item = state.thingParameters[event.parameterIndex]
        item = item.copy(value = event.value)
        state.thingParameters[event.parameterIndex] = item
        isValid()
    }

    private fun parameterSetParameterValueChanged(event: ThingFormEvent.ParameterSetParameterValueChanged) {
        var item = state
            .thingParameterSets[event.parameterSetIndex]
            .thingParameters[event.parameterSetParameterIndex]
        item = item.copy(value = event.value)
        state
            .thingParameterSets[event.parameterSetIndex]
            .thingParameters[event.parameterSetParameterIndex] = item
        isValid()
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
                val actualThingId = if(!isEditMode) {
                    //create
                    thingUseCases.add(thing, state.image)
                } else {
                    //update
                    thingUseCases.update(thing, state.image)
                    thingId!!
                }

                thingParameterParameterSetUseCases.changeThingParameters(
                    actualThingId, state.thingParameters
                )

                thingParameterSetUseCases.changeThingParameterSets(
                    actualThingId, state.thingParameterSets.map {
                        it.toThingParameterSet()
                    }
                )
                delay(2000)
                state = state.copy(saveStatus = SaveStatus.Saved)
            }
        }
    }

    // endregion Form events
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
     * Check if all validations have passed (not run validations!)
     * If not, disable saving
     * Else, enable saving
     */
    private fun isValid(): Boolean {
        var isValid = true
        if (!nameValidated) {
            isValid = false
        } else {
            with(state) {
                val validations = arrayOf(nameValidation)
                if (validations.any { it != null }) {
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

    // endregion Form

    // region Parameters selection
    var parametersState by mutableStateOf(ThingParametersState())
        private set

    private var parametersSearchJob: Job? = null
    private var parametersGetListJob: Job? = null

    fun parametersScreenStart() {
        parametersGetList()
    }

    fun parametersOnEvent(event: ThingParametersEvent) {
        when (event) {
            is ThingParametersEvent.QueryChanged -> parametersSearch(event.query)
            is ThingParametersEvent.SelectItem -> with(event) {
                parametersSelectItem(
                    id,
                    selected,
                    index
                )
            }

            is ThingParametersEvent.UnselectAll -> parametersUnselectAll()
        }
    }

    /**
     * Search parameter list
     * @param query Query
     */
    private fun parametersSearch(query: String) {
        parametersState = parametersState.copy(query = query)
        if (query.isNotBlank()) {
            // Cancel existing searches
            parametersSearchJob?.cancel()
            parametersGetListJob?.cancel()

            parametersSearchJob = viewModelScope.launch {
                delay(200) // don't search for 200ms so typing more cancels jobs
                parametersState = parametersState.copy(
                    parameterList = parameterUseCases.search(query).first()
                        .parametersSelectSelected()
                        .toMutableStateList()
                )
            }
        } else {
            parametersGetList()
        }
    }

    private fun parametersGetList() {
        parametersSearchJob?.cancel()
        if (parametersGetListJob?.isActive != true) {
            parametersGetListJob = viewModelScope.launch {
                val parameters = parameterUseCases.getListFlow().first()
                    .parametersSelectSelected()
                    .toMutableStateList()
                parametersState = parametersState.copy(
                    parameterList = parameters
                )
            }
        }

    }

    /**
     * Select an item
     * @param id Id of selected item
     * @param selected Whether item is selected or not
     * @param index index of item in list
     */
    private fun parametersSelectItem(id: Int, selected: Boolean, index: Int? = null) {
        var item: ParameterListItem
        val itemIndex: Int
        if (index == null) {
            // find item and index if index not given
            item = parametersState.parameterList.first { it.id == id }
            itemIndex = parametersState.parameterList.indexOf(item)
        } else {
            // get the item, if index is given
            itemIndex = index
            item = parametersState.parameterList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        parametersState.parameterList[itemIndex] = item
        // update selected parameters list
        viewModelScope.launch {
            val newParameters =
                if (selected) {
                    selectedParametersIdName + ParameterIdName(
                        item.id,
                        item.name
                    )
                } else {
                    selectedParametersIdName - ParameterIdName(
                        item.id,
                        item.name
                    )
                }
            selectedParametersIdNameFlow.emit(
                newParameters
            )
            isValid()
        }
    }

    private fun parametersUnselectAll() {
        viewModelScope.launch {
            selectedParametersIdNameFlow.emit(emptyList())
        }
        parametersState.parameterList.replaceAll { item -> item.copy(selected = false) }
        viewModelScope.launch {
            isValid()
        }
    }

    private fun List<ParameterListItem>.parametersSelectSelected(): List<ParameterListItem> {
        return map { item ->
            item.copy(
                selected = selectedParametersIdName.any {
                    it.id == item.id
                }
            )
        }
    }

    // endregion Parameters selection

    // region Parameter sets selection
    var parameterSetsState by mutableStateOf(ThingParameterSetsState())
        private set

    private var parameterSetsSearchJob: Job? = null
    private var parameterSetsGetListJob: Job? = null

    fun parameterSetsScreenStart() {
        parameterSetsGetList()
    }

    fun parameterSetsOnEvent(event: ThingParameterSetsEvent) {
        when (event) {
            is ThingParameterSetsEvent.QueryChanged -> parameterSetsSearch(event.query)
            is ThingParameterSetsEvent.SelectItem -> with(event) {
                parameterSetsSelectItem(
                    id,
                    selected,
                    index
                )
            }

            is ThingParameterSetsEvent.UnselectAll -> parameterSetsUnselectAll()
        }
    }

    /**
     * Search parameter set list
     * @param query Query
     */
    private fun parameterSetsSearch(query: String) {
        parameterSetsState = parameterSetsState.copy(query = query)
        if (query.isNotBlank()) {
            // Cancel existing searches
            parameterSetsSearchJob?.cancel()
            parameterSetsGetListJob?.cancel()

            parameterSetsSearchJob = viewModelScope.launch {
                delay(200) // don't search for 200ms so typing more cancels jobs
                parameterSetsState = parameterSetsState.copy(
                    parameterSetList = parameterSetUseCases.search(query).first()
                        .parameterSetsSelectSelected()
                        .toMutableStateList()
                )
            }
        } else {
            parameterSetsGetList()
        }
    }

    private fun parameterSetsGetList() {
        parameterSetsSearchJob?.cancel()
        if (parameterSetsGetListJob?.isActive != true) {
            parameterSetsGetListJob = viewModelScope.launch {
                val parameterSets = parameterSetUseCases.getListFlow().first()
                    .parameterSetsSelectSelected()
                    .toMutableStateList()
                parameterSetsState = parameterSetsState.copy(
                    parameterSetList = parameterSets
                )
            }
        }

    }

    /**
     * Select an item
     * @param id Id of selected item
     * @param selected Whether item is selected or not
     * @param index index of item in list
     */
    private fun parameterSetsSelectItem(id: Int, selected: Boolean, index: Int? = null) {
        var item: ParameterSetListItem
        val itemIndex: Int
        if (index == null) {
            // find item and index if index not given
            item = parameterSetsState.parameterSetList.first { it.id == id }
            itemIndex = parameterSetsState.parameterSetList.indexOf(item)
        } else {
            // get the item, if index is given
            itemIndex = index
            item = parameterSetsState.parameterSetList[itemIndex]
        }
        // update item
        item = item.copy(selected = selected)
        parameterSetsState.parameterSetList[itemIndex] = item
        // update selected parameter set list
        viewModelScope.launch {
            val newParameterSets =
                if (selected) {
                    selectedParameterSetsIdName + ParameterSetIdName(
                        item.id,
                        item.name
                    )
                } else {
                    selectedParameterSetsIdName - ParameterSetIdName(
                        item.id,
                        item.name
                    )
                }
            selectedParameterSetsIdNameFlow.emit(
                newParameterSets
            )
            isValid()
        }
    }

    private fun parameterSetsUnselectAll() {
        viewModelScope.launch {
            selectedParameterSetsIdNameFlow.emit(emptyList())
        }
        parameterSetsState.parameterSetList.replaceAll { item -> item.copy(selected = false) }
        viewModelScope.launch {
            isValid()
        }
    }

    private fun List<ParameterSetListItem>.parameterSetsSelectSelected(): List<ParameterSetListItem> {
        return map { item ->
            item.copy(
                selected = selectedParameterSetsIdName.any {
                    it.id == item.id
                }
            )
        }
    }

    // endregion Parameter sets selection
    init {
        formInit()
    }

    private data class ParameterIdName(
        val id: Int,
        val name: String,
    )

    private data class ParameterSetIdName(
        val id: Int,
        val name: String,
    )
}