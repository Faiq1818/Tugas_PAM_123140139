package com.pertemuan3.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.pertemuan3.ProfileUiState
import org.jetbrains.compose.resources.painterResource
import pertemuan_3.composeapp.generated.resources.Res
import pertemuan_3.composeapp.generated.resources.profile

@Composable
fun ProfileScreen(
    profile: ProfileUiState,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onSaveProfile: (name: String, bio: String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        ProfileCard(
            profile = profile,
            isDarkMode = isDarkMode,
            onToggleTheme = onToggleTheme,
            onSaveProfile = onSaveProfile
        )
    }
}

@Composable
fun ProfileCard(
    profile: ProfileUiState,
    isDarkMode: Boolean,
    onToggleTheme: () -> Unit,
    onSaveProfile: (name: String, bio: String) -> Unit
) {
    // State hoisting: TextField values dikelola di sini, bukan di EditProfileForm
    var isEditing by remember { mutableStateOf(false) }
    var nameInput by remember(profile.name) { mutableStateOf(profile.name) }
    var bioInput by remember(profile.bio) { mutableStateOf(profile.bio) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surface)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                IconButton(onClick = onToggleTheme) {
                    Icon(
                        imageVector = if (isDarkMode) Icons.Default.LightMode else Icons.Default.DarkMode,
                        contentDescription = "Toggle Theme",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            ProfileHeader(name = profile.name, bio = profile.bio)
            Spacer(modifier = Modifier.height(16.dp))

            InfoItem("Email", profile.email)
            InfoItem("Phone", profile.phone)
            InfoItem("Location", profile.location)

            Spacer(modifier = Modifier.height(20.dp))

            if (isEditing) {
                HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                // State (nameInput, bioInput) di-hoist ke sini, diteruskan ke form
                EditProfileForm(
                    name = nameInput,
                    bio = bioInput,
                    onNameChange = { nameInput = it },
                    onBioChange = { bioInput = it },
                    onSave = {
                        onSaveProfile(nameInput, bioInput)
                        isEditing = false
                    },
                    onCancel = {
                        nameInput = profile.name
                        bioInput = profile.bio
                        isEditing = false
                    }
                )
            } else {
                Button(
                    onClick = { isEditing = true },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text("Edit Profile")
                }
            }
        }
    }
}

@Composable
fun ProfileHeader(name: String, bio: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Image(
            painter = painterResource(Res.drawable.profile),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = name, style = MaterialTheme.typography.titleLarge)
        Spacer(modifier = Modifier.height(6.dp))
        Text(text = bio, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun InfoItem(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Text(text = value, style = MaterialTheme.typography.bodyMedium)
    }
}
