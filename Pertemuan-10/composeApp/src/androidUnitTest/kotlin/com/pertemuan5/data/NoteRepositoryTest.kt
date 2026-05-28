package com.pertemuan5.data

import androidx.test.core.app.ApplicationProvider
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.pertemuan5.database.AppDatabase
import com.pertemuan5.database.NoteEntity
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [34])
class NoteRepositoryTest {

    private lateinit var repository: NoteRepository
    private lateinit var database: AppDatabase
    private lateinit var driver: AndroidSqliteDriver

    @Before
    fun setup() {
        driver = AndroidSqliteDriver(
            schema = AppDatabase.Schema,
            context = ApplicationProvider.getApplicationContext(),
            name = null // in-memory
        )
        database = AppDatabase(driver)
        repository = NoteRepository(database)
    }

    @After
    fun tearDown() {
        driver.close()
    }

    @Test
    fun `insert and get all notes`() = runBlocking {
        repository.insertNote("Title 1", "Body 1", 100L)
        repository.insertNote("Title 2", "Body 2", 200L)

        val notes = repository.getAllNotes().first()
        
        assertEquals(2, notes.size)
        // Ordered by createdAt DESC
        assertEquals("Title 2", notes[0].title)
        assertEquals("Title 1", notes[1].title)
    }

    @Test
    fun `getAllNotesOrderByTitle returns sorted notes`() = runBlocking {
        repository.insertNote("B", "Body")
        repository.insertNote("A", "Body")

        val notes = repository.getAllNotesOrderByTitle().first()
        
        assertEquals(2, notes.size)
        assertEquals("A", notes[0].title)
        assertEquals("B", notes[1].title)
    }

    @Test
    fun `searchNotes returns matching notes`() = runBlocking {
        repository.insertNote("Apple", "Fruit")
        repository.insertNote("Banana", "Fruit")
        repository.insertNote("Carrot", "Vegetable")

        val results = repository.searchNotes("App").first()
        
        assertEquals(1, results.size)
        assertEquals("Apple", results[0].title)
    }

    @Test
    fun `updateNote modifies existing note`() = runBlocking {
        repository.insertNote("Old Title", "Old Body")
        val initialNotes = repository.getAllNotes().first()
        val id = initialNotes[0].id

        repository.updateNote(id, "New Title", "New Body")
        
        val updatedNotes = repository.getAllNotes().first()
        assertEquals("New Title", updatedNotes[0].title)
        assertEquals("New Body", updatedNotes[0].body)
    }

    @Test
    fun `deleteNote removes note`() = runBlocking {
        repository.insertNote("To Delete", "Body")
        val initialNotes = repository.getAllNotes().first()
        val id = initialNotes[0].id

        repository.deleteNote(id)
        
        val finalNotes = repository.getAllNotes().first()
        assertTrue(finalNotes.isEmpty())
    }
}
