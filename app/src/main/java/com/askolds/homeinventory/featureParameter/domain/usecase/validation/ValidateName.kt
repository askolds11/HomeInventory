package com.askolds.homeinventory.featureParameter.domain.usecase.validation

import com.askolds.homeinventory.featureParameter.data.repository.ParameterRepository

class ValidateName(
    private val repository: ParameterRepository
) {
    /**
     * Validates the Parameter's name
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