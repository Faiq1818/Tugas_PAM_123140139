package com.pertemuan5.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pertemuan5.data.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val isDarkMode: StateFlow<Boolean> = settingsRepository.isDarkMode
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    val isSortByTitle: StateFlow<Boolean> = settingsRepository.sortByTitle
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = false
        )

    fun toggleDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setDarkMode(enabled)
        }
    }

    fun toggleSortByTitle(enabled: Boolean) {
        viewModelScope.launch {
            settingsRepository.setSortByTitle(enabled)
        }
    }
}
