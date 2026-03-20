package com.pertemuan3

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.resources.painterResource

import pertemuan_3.composeapp.generated.resources.Res
import pertemuan_3.composeapp.generated.resources.profile

data class Profile(
    val name: String,
    val bio: String,
    val email: String,
    val phone: String,
    val location: String
)

@Composable
@Preview
fun App() {
    val myColorScheme = lightColorScheme(
        primary = Color(0xFFFFFFFF),
        primaryContainer = Color(0xFFf5f5f5),
        secondary = Color(0xFF212121)
    )

    MaterialTheme(colorScheme = myColorScheme) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primaryContainer),
            contentAlignment = Alignment.Center
        ) {
            ProfileCard()
        }
    }
}

@Composable
fun ProfileCard() {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            ProfileHeader(
                name = "Faiq Ghozy Erlangga",
                bio = "Informatics Student • Linux • NixOS • Full Stack Developer"
            )
            Spacer(modifier = Modifier.height(16.dp))

            InfoItem("Email", "faiq@email.com")
            InfoItem("Phone", "+62 812 3456 7890")
            InfoItem("Location", "Lampung, Indonesia")

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = Color.White
                )            ) {
                Text("Contact Me")
            }
        }
    }
}

@Composable
fun ProfileHeader(
    name: String,
    bio: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(Res.drawable.profile),
            contentDescription = "Profile Image",
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = name,
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = bio,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun InfoItem(
    label: String,
    value: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium
        )

        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
