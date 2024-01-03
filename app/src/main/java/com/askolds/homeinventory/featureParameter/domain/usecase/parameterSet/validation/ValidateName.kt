package com.askolds.homeinventory.featureParameter.domain.usecase.parameterSet.validation

import com.askolds.homeinventory.featureParameter.data.repository.ParameterSetRepository

class ValidateName(
    private val repository: ParameterSetRepository
) {
    /**
     * Validates the Parameter set's name
     * All validations based on the trimmed name.
     */
    suspend operator fun invoke(name: String?, originalName: String?): ERROR? {
        val trimmedName = name?.trim()
        if (trimmedName.isNullOrBlank()) {
            return ERROR.NULL_OR_BLANK
        }
        if (repository.nameExists(trimmedName, originalName)) {
            return ERROR.ALREADY_EXISTS
        }
        return null
    }

    enum class ERROR {
        NULL_OR_BLANK, ALREADY_EXISTS
    }
}