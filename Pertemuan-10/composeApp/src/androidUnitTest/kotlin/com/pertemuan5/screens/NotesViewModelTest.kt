package com.pertemuan5.screens

import app.cash.turbine.test
import com.pertemuan5.data.NoteRepository
import com.pertemuan5.data.SettingsRepository
import com.pertemuan5.database.NoteEntity
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class NotesViewModelTest {

    private val noteRepository: NoteRepository = mockk()
    private val settingsRepository: SettingsRepository = mockk()
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { settingsRepository.sortByTitle } returns flowOf(false)
        every { noteRepository.getAllNotes() } returns flowOf(emptyList())
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial uiState is Loading`() = runTest {
        val viewModel = NotesViewModel(noteRepository, settingsRepository)
        assertEquals(NotesUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `uiState becomes Success when notes are available`() = runTest {
        val notes = listOf(NoteEntity(1, "Title", "Body", 123L))
        every { noteRepository.getAllNotes() } returns flowOf(notes)
        
        val viewModel = NotesViewModel(noteRepository, settingsRepository)
        
        viewModel.uiState.test {
            assertEquals(NotesUiState.Loading, awaitItem())
            val state = awaitItem()
            assertTrue(state is NotesUiState.Success)
            assertEquals(notes, (state as NotesUiState.Success).notes)
        }
    }

    @Test
    fun `uiState becomes Empty when no notes available`() = runTest {
        every { noteRepository.getAllNotes() } returns flowOf(emptyList())
        
        val viewModel = NotesViewModel(noteRepository, settingsRepository)
        
        viewModel.uiState.test {
            assertEquals(NotesUiState.Loading, awaitItem())
            assertEquals(NotesUiState.Empty, awaitItem())
        }
    }

    @Test
    fun `updateSearchQuery calls searchNotes when query is not blank`() = runTest {
        val query = "search"
        val notes = listOf(NoteEntity(1, "Search", "Body", 123L))
        every { noteRepository.searchNotes(query) } returns flowOf(notes)
        
        val viewModel = NotesViewModel(noteRepository, settingsRepository)
        viewModel.updateSearchQuery(query)
        
        viewModel.uiState.test {
            // Skip initial and maybe middle states
            var lastState = awaitItem()
            while(lastState !is NotesUiState.Success) {
                lastState = awaitItem()
            }
            assertEquals(notes, (lastState as NotesUiState.Success).notes)
        }
    }

    @Test
    fun `addNote calls repository insertNote`() = runTest {
        coEvery { noteRepository.insertNote(any(), any(), any()) } returns Unit
        val viewModel = NotesViewModel(noteRepository, settingsRepository)
        
        viewModel.addNote("Title", "Body")
        testDispatcher.scheduler.advanceUntilIdle()
        
        io.mockk.coVerify { noteRepository.insertNote("Title", "Body", any()) }
    }
}
