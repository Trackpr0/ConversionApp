package com.agustin.converter.ui

import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.semantics.getOrNull
import androidx.compose.ui.test.SemanticsNodeInteraction
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import com.agustin.converter.MainActivity
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlin.math.abs
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ComposeConversionTest {

    @get:Rule(order = 0)
    val hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val compose = createAndroidComposeRule<MainActivity>()

    @Before
    fun setup() {
        hiltRule.inject()
    }

    /** Helper to read the visible text from a node */
    private fun SemanticsNodeInteraction.readText(): String =
        fetchSemanticsNode().config
            .getOrNull(SemanticsProperties.Text)
            ?.joinToString("") { it.text }
            ?: ""

    @Test
    fun convertsCelsiusToFahrenheit_defaultDropdowns() {
        // Type 100 into the input field (tag added in the screen)
        compose.onNodeWithTag("input", useUnmergedTree = true)
            .performTextInput("100")

        // Tap Convert
        compose.onNodeWithTag("convertBtn")
            .performClick()

        // Wait until the result actually shows "Result: <number>"
        compose.waitUntil(5_000) {
            val t = compose.onNodeWithTag("result").readText()
            t.startsWith("Result:") && t.substringAfter("Result:").trim().isNotEmpty()
        }

        // Read the result and assert it's ~212.0
        val text = compose.onNodeWithTag("result").readText()
        val numberStr = text.substringAfter("Result:").trim()
            .takeWhile { it.isDigit() || it == '.' || it == '-' }
        val value = numberStr.toDoubleOrNull()

        assert(value != null && abs(value - 212.0) < 0.2) {
            "Expected around 212.0, but got '$text'"
        }
    }
}
