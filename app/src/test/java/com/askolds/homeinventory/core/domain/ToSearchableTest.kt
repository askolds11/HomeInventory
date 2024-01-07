package com.askolds.homeinventory.core.domain

import org.junit.Assert.*

import org.junit.Test

class ToSearchableTest {

    @Test(expected = IllegalArgumentException::class)
    fun toSearchable_emptyString_throwsIllegalArgumentException() {
        val string = " "
        string.toSearchable()
    }

    @Test
    fun toSearchable_paddedString_returnsTrimmedString() {
        val string = " test "
        val result = string.toSearchable()
        assertEquals("test", result)
    }

    @Test
    fun toSearchable_accents_returnsNoAccents() {
        val string = "āčēģīķļņšūž"
        val result = string.toSearchable()
        assertEquals("acegiklnsuz", result)
    }

    @Test
    fun toSearchable_caps_returnsNoCaps() {
        val string = "ANGRY"
        val result = string.toSearchable()
        assertEquals("angry", result)
    }
}