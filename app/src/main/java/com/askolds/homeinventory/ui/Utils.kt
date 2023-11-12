package com.askolds.homeinventory.ui

import java.text.Normalizer

fun String.stripAccents(): String {
    var s = this
    s = Normalizer.normalize(s, Normalizer.Form.NFD)
    s = s.replace("\\p{InCombiningDiacriticalMarks}".toRegex(), "")
    return s
}