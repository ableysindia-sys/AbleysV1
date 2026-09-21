package com.ableys.app

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import com.ableys.app.ui.components.EquipmentCheckSheet
import com.ableys.app.ui.theme.AbleysTheme
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class EquipmentCheckTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun `the question names the item and both answers are offered`() {
        composeTestRule.setContent {
            AbleysTheme {
                EquipmentCheckSheet(
                    equipmentName = "Platform Swing",
                    householdAlternative = "A folded blanket on a low stool",
                    onAnswer = {},
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("equipment_check_sheet").assertIsDisplayed()
        composeTestRule.onNodeWithText("Do you have a Platform Swing at home?").assertIsDisplayed()
        composeTestRule.onNodeWithTag("equipment_check_yes").assertIsDisplayed()
        composeTestRule.onNodeWithTag("equipment_check_no").assertIsDisplayed()
    }

    @Test
    fun `declining points at the home version rather than the shop`() {
        var answered: Boolean? = null
        composeTestRule.setContent {
            AbleysTheme {
                EquipmentCheckSheet(
                    equipmentName = "Stepping Stones",
                    householdAlternative = "Folded dupattas on the floor",
                    onAnswer = { answered = it },
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Not yet — show the home version").assertIsDisplayed()
        composeTestRule.onNodeWithTag("equipment_check_no").performClick()
        assertEquals(false, answered)
    }

    @Test
    fun `an item with no household alternative still offers a way out`() {
        composeTestRule.setContent {
            AbleysTheme {
                EquipmentCheckSheet(
                    equipmentName = "Weighted Blanket",
                    householdAlternative = null,
                    onAnswer = {},
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithText("Not yet").assertIsDisplayed()
    }

    @Test
    fun `answering yes reports ownership`() {
        var answered: Boolean? = null
        composeTestRule.setContent {
            AbleysTheme {
                EquipmentCheckSheet(
                    equipmentName = "Platform Swing",
                    householdAlternative = null,
                    onAnswer = { answered = it },
                    onDismiss = {}
                )
            }
        }
        composeTestRule.onNodeWithTag("equipment_check_yes").performClick()
        assertTrue(answered == true)
    }
}
