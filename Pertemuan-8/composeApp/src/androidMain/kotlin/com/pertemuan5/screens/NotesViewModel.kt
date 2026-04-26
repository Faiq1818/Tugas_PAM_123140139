package com.pertemuan5.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pertemuan5.data.NoteRepository
import com.pertemuan5.data.SettingsRepository
import com.pertemuan5.database.NoteEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

sealed class NotesUiState {
    object Loading : NotesUiState()
    data class Success(val notes: List<NoteEntity>) : NotesUiState()
    object Empty : NotesUiState()
}

class NotesViewModel(
    private val noteRepository: NoteRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val notesFlow = combine(
        _searchQuery,
        settingsRepository.sortByTitle
    ) { query, sortByTitle ->
        Pair(query, sortByTitle)
    }.flatMapLatest { (query, sortByTitle) ->
        when {
            query.isNotBlank() -> noteRepository.searchNotes(query)
            sortByTitle -> noteRepository.getAllNotesOrderByTitle()
            else -> noteRepository.getAllNotes()
        }
    }

    val uiState: StateFlow<NotesUiState> = notesFlow
        .combine(_searchQuery) { notes, _ ->
            if (notes.isEmpty()) NotesUiState.Empty else NotesUiState.Success(notes)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = NotesUiState.Loading
        )

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun addNote(title: String, body: String) {
        viewModelScope.launch {
            noteRepository.insertNote(title, body)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            noteRepository.deleteNote(id)
        }
    }

    fun updateNote(id: Long, title: String, body: String) {
        viewModelScope.launch {
            noteRepository.updateNote(id, title, body)
        }
    }
}
