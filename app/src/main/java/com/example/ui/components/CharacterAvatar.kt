package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard

@Composable
fun CharacterAvatar(
    avatarEmoji: String,
    photoUri: String?,
    size: Dp = 56.dp,
    isSelected: Boolean = false,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) DigitalCyan else ScaleNavyBorder
    val borderWidth = if (isSelected) 2.5.dp else 1.5.dp

    Box(
        modifier = modifier
            .size(size)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFF223657),
                        Color(0xFF142035)
                    )
                )
            )
            .border(borderWidth, borderColor, CircleShape),
        contentAlignment = Alignment.Center
    ) {
        if (!photoUri.isNullOrBlank()) {
            AsyncImage(
                model = photoUri,
                contentDescription = "Foto Karakter",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
            )
        } else {
            Text(
                text = avatarEmoji.ifBlank { "👤" },
                fontSize = (size.value * 0.48).sp
            )
        }
    }
}
