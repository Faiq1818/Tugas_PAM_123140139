package com.pertemuan5.screens

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import com.pertemuan5.database.NoteEntity
import com.pertemuan5.platform.NetworkMonitor
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.MutableStateFlow
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NotesScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val viewModel: NotesViewModel = mockk(relaxed = true)
    private val networkMonitor: NetworkMonitor = mockk()

    private val uiStateFlow = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    private val searchQueryFlow = MutableStateFlow("")
    private val isConnectedFlow = MutableStateFlow(true)

    @Before
    fun setup() {
        every { viewModel.uiState } returns uiStateFlow
        every { viewModel.searchQuery } returns searchQueryFlow
        every { networkMonitor.isConnected } returns isConnectedFlow
    }

    @Test
    fun `loading state shows loading indicator`() {
        uiStateFlow.value = NotesUiState.Loading
        
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, networkMonitor = networkMonitor)
        }

        composeTestRule.onNodeWithTag("loadingIndicator").assertIsDisplayed()
    }

    @Test
    fun `empty state shows empty message`() {
        uiStateFlow.value = NotesUiState.Empty
        
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, networkMonitor = networkMonitor)
        }

        composeTestRule.onNodeWithTag("emptyState").assertIsDisplayed()
        composeTestRule.onNodeWithText("No notes yet. Tap + to add one.").assertIsDisplayed()
    }

    @Test
    fun `success state shows notes list`() {
        val notes = listOf(NoteEntity(1, "Test Note", "Body", 123L))
        uiStateFlow.value = NotesUiState.Success(notes)
        
        composeTestRule.setContent {
            NotesScreen(viewModel = viewModel, networkMonitor = networkMonitor)
        }

        composeTestRule.onNodeWithTag("notesList").assertIsDisplayed()
        composeTestRule.onNodeWithText("Test Note").assertIsDisplayed()
    }
}
