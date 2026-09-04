package com.example.englishapplication.presentation.paragraph_add

import org.junit.Assert.assertEquals
import org.junit.Test

class MergeScannedTextTest {
    @Test
    fun collapsesLineBreaksIntoOneBlock() {
        assertEquals("Hello world again", mergeScannedText("", "Hello\nworld \n  again\n"))
    }

    @Test
    fun appendsToExistingText() {
        assertEquals("Old text new", mergeScannedText("Old text ", "new"))
    }
}
