package com.askolds.homeinventory.domain

import java.text.Normalizer

fun String.stripAccents(): String {
    var s = this
    s = Normalizer.normalize(s, Normalizer.Form.NFD)
    s = s.replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")
    return s
}

fun String.toSearchable(): String {
    return this.trim().stripAccents().lowercase()
}