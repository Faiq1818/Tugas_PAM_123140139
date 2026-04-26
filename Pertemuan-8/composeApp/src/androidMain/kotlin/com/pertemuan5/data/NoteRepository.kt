package com.pertemuan5.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.pertemuan5.database.AppDatabase
import com.pertemuan5.database.NoteEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class NoteRepository(db: AppDatabase) {
    private val queries = db.noteQueries

    fun getAllNotes(): Flow<List<NoteEntity>> {
        return queries.selectAll().asFlow().mapToList(Dispatchers.IO)
    }

    fun getAllNotesOrderByTitle(): Flow<List<NoteEntity>> {
        return queries.selectAllOrderByTitle().asFlow().mapToList(Dispatchers.IO)
    }

    fun searchNotes(query: String): Flow<List<NoteEntity>> {
        return queries.searchByTitleOrBody(query).asFlow().mapToList(Dispatchers.IO)
    }

    suspend fun insertNote(title: String, body: String, createdAt: Long = System.currentTimeMillis()) {
        withContext(Dispatchers.IO) {
            queries.insert(title, body, createdAt)
        }
    }

    suspend fun updateNote(id: Long, title: String, body: String) {
        withContext(Dispatchers.IO) {
            queries.update(title, body, id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteById(id)
        }
    }
}
