package com.pertemuan3

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pertemuan3.ui.ProfileScreen
import com.pertemuan3.viewmodel.ProfileViewModel

private val LightColors = lightColorScheme(
    background = Color(0xFFf5f5f5),
    surface = Color(0xFFFFFFFF),
    onSurface = Color(0xFF212121),
    primary = Color(0xFF212121),
    onPrimary = Color(0xFFFFFFFF)
)

private val DarkColors = darkColorScheme(
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFEEEEEE),
    primary = Color(0xFFBB86FC),
    onPrimary = Color(0xFF000000)
)

@Composable
@Preview
fun App(profileViewModel: ProfileViewModel = viewModel()) {
    val isDarkMode = profileViewModel.themeState.isDarkMode

    MaterialTheme(colorScheme = if (isDarkMode) DarkColors else LightColors) {
        ProfileScreen(
            profile = profileViewModel.profileState,
            isDarkMode = isDarkMode,
            onToggleTheme = profileViewModel::toggleTheme,
            onSaveProfile = profileViewModel::updateProfile
        )
    }
}
