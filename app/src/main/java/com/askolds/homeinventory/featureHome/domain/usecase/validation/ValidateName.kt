package com.askolds.homeinventory.featureHome.domain.usecase.validation

import com.askolds.homeinventory.featureHome.data.repository.HomeRepository

class ValidateName(
    private val repository: HomeRepository
) {
    /**
     * Validates the Home's name
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