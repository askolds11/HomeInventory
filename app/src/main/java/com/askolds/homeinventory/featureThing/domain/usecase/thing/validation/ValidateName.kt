package com.askolds.homeinventory.featureThing.domain.usecase.thing.validation

// leaving suspend and originalName to match other validations, although not used

class ValidateName {
    /**
     * Validates the Thing's name
     * All validations based on the trimmed name.
     */
    suspend operator fun invoke(name: String?, originalName: String?): ERROR? {
        val trimmedName = name?.trim()
        if (trimmedName.isNullOrBlank()) {
            return ERROR.NULL_OR_BLANK
        }
        return null
    }

    enum class ERROR {
        NULL_OR_BLANK
    }
}