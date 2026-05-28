package com.pertemuan5.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Notes : Screen("notes", "Notes", Icons.Default.List)
    object Favorites : Screen("favorites", "Favorites", Icons.Default.Favorite)
    object Settings : Screen("settings", "Settings", Icons.Default.Settings)
}

val navItems = listOf(
    Screen.Notes,
    Screen.Favorites,
    Screen.Settings
)
