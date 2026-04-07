package com.pertemuan3.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.pertemuan3.ProfileUiState
import com.pertemuan3.data.ThemeState

class ProfileViewModel : ViewModel() {
    var themeState by mutableStateOf(ThemeState())
        private set

    var profileState by mutableStateOf(
        ProfileUiState(
            name = "Faiq Ghozy Erlangga",
            bio = "Informatics Student • Linux • NixOS • Full Stack Developer",
            email = "faiq@email.com",
            phone = "+62 812 3456 7890",
            location = "Lampung, Indonesia"
        )
    )
        private set

    fun toggleTheme() {
        themeState = themeState.copy(isDarkMode = !themeState.isDarkMode)
    }

    fun updateProfile(name: String, bio: String) {
        profileState = profileState.copy(name = name.trim(), bio = bio.trim())
    }
}
