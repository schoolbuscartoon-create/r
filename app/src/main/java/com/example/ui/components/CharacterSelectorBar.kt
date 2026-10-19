package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyCharacter
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard

@Composable
fun CharacterSelectorBar(
    characters: List<FamilyCharacter>,
    selectedCharacter: FamilyCharacter?,
    onSelectCharacter: (FamilyCharacter) -> Unit,
    onAddCharacterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Pilih Karakter Pengguna",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFBACADB)
            )

            Text(
                text = "${characters.size} Anggota",
                fontSize = 11.sp,
                color = DigitalCyan
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(characters, key = { it.id }) { character ->
                val isSelected = selectedCharacter?.id == character.id

                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isSelected) Color(0xFF1B2D4B) else ScaleNavyCard,
                    border = androidx.compose.foundation.BorderStroke(
                        width = if (isSelected) 1.5.dp else 1.dp,
                        color = if (isSelected) DigitalCyan else ScaleNavyBorder
                    ),
                    modifier = Modifier
                        .clickable { onSelectCharacter(character) }
                        .testTag("character_chip_${character.id}")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        CharacterAvatar(
                            avatarEmoji = character.avatarEmoji,
                            photoUri = character.photoUri,
                            size = 40.dp,
                            isSelected = isSelected
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = character.name,
                                fontSize = 13.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.SemiBold,
                                color = if (isSelected) Color.White else Color(0xFFCBD5E1)
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(
                                            if (isSelected) DigitalCyanGlow else Color(0x33475569)
                                        )
                                        .padding(horizontal = 5.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = character.role,
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (isSelected) DigitalCyan else Color(0xFF94A3B8)
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "${character.age} thn",
                                    fontSize = 10.sp,
                                    color = Color(0xFF94A3B8)
                                )
                            }
                        }
                    }
                }
            }

            // Add new character button
            item {
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color(0x1400E5FF),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DigitalCyan.copy(alpha = 0.4f)),
                    modifier = Modifier
                        .height(56.dp)
                        .clickable { onAddCharacterClick() }
                        .testTag("add_character_chip_button")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(DigitalCyanGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Tambah Karakter",
                                tint = DigitalCyan,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "+ Karakter",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = DigitalCyan
                        )
                    }
                }
            }
        }
    }
}
