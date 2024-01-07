package com.askolds.homeinventory.core.domain

import java.text.Normalizer

fun String.stripAccents(): String {
    var s = this
    s = Normalizer.normalize(s, Normalizer.Form.NFD)
    s = s.replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")
    return s
}

fun String.toSearchable(): String {
    if (this.isBlank()) {
        throw IllegalArgumentException("String must be non blank")
    }
    return this.trim().stripAccents().lowercase()
}