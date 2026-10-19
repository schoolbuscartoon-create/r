package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleUtils
import com.example.ui.ScaleViewModel
import com.example.ui.components.CharacterAvatar
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

@Composable
fun CharactersScreen(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    val characters by viewModel.characters.collectAsState()
    val selectedCharacter by viewModel.selectedCharacter.collectAsState()
    var characterToDelete by remember { mutableStateOf<FamilyCharacter?>(null) }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScaleNavyDeep)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Group,
                            contentDescription = null,
                            tint = DigitalCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Karakter Keluarga",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "Kelola profil & parameter default anggota",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }

                Button(
                    onClick = { viewModel.openAddCharacterDialog() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = DigitalCyan,
                        contentColor = Color(0xFF04101A)
                    ),
                    shape = RoundedCornerShape(12.dp),
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp),
                    modifier = Modifier.testTag("add_character_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(text = "Tambah", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }

            if (characters.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Belum ada karakter",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Tekan tombol + Tambah untuk membuat profil baru",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(characters, key = { it.id }) { character ->
                        val isSelected = selectedCharacter?.id == character.id
                        CharacterCard(
                            character = character,
                            isSelected = isSelected,
                            onSelect = { viewModel.selectCharacter(character) },
                            onEdit = { viewModel.openEditCharacterDialog(character) },
                            onDelete = { characterToDelete = character },
                            onPhotoClick = { character.photoUri?.let { viewModel.openPhotoPreview(it) } }
                        )
                    }
                }
            }
        }

        // Delete confirmation dialog
        characterToDelete?.let { char ->
            AlertDialog(
                onDismissRequest = { characterToDelete = null },
                title = { Text("Hapus Karakter?") },
                text = { Text("Karakter \"${char.name}\" beserta riwayat timbangannya akan dihapus secara permanen.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteCharacter(char)
                            characterToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF43F5E))
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { characterToDelete = null }) {
                        Text("Batal")
                    }
                },
                containerColor = ScaleNavyCard,
                titleContentColor = Color.White,
                textContentColor = Color(0xFFCBD5E1)
            )
        }
    }
}

@Composable
private fun CharacterCard(
    character: FamilyCharacter,
    isSelected: Boolean,
    onSelect: () -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    onPhotoClick: () -> Unit
) {
    val weightStr = ScaleUtils.formatWeightDigital(character.defaultWeightKg)
    val heightStr = ScaleUtils.formatHeight(character.heightCm)
    val bmiStr = ScaleUtils.formatBmiDigital(character.defaultBmi)
    val bmiCat = ScaleUtils.getBmiCategory(character.defaultBmi)

    Surface(
        shape = RoundedCornerShape(20.dp),
        color = if (isSelected) Color(0xFF152540) else ScaleNavyCard,
        border = androidx.compose.foundation.BorderStroke(
            1.5.dp,
            if (isSelected) DigitalCyan else ScaleNavyBorder
        ),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("character_card_${character.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Row: Avatar + Info + Actions
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier.clickable { onPhotoClick() }
                ) {
                    CharacterAvatar(
                        avatarEmoji = character.avatarEmoji,
                        photoUri = character.photoUri,
                        size = 56.dp,
                        isSelected = isSelected
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = character.name,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        if (isSelected) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(DigitalCyanGlow)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = "AKTIF",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = DigitalCyan
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(0xFF223657))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = character.role,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = DigitalCyan
                            )
                        }

                        Text(
                            text = "•",
                            color = Color(0xFF64748B),
                            fontSize = 10.sp
                        )

                        Text(
                            text = character.gender,
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                    }
                }

                // Edit & Delete icons
                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Karakter",
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier.size(34.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.DeleteOutline,
                            contentDescription = "Hapus Karakter",
                            tint = Color(0xFFF43F5E),
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Parameter Details Row
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFF0D1726))
                    .border(1.dp, Color(0xFF1E2D44), RoundedCornerShape(12.dp))
                    .padding(10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "DOB: ${character.dob}",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFFCBD5E1)
                    )
                    Text(
                        text = "Usia: ${character.age} Tahun",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF10B981)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Tinggi: $heightStr",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "Berat: $weightStr kg",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        color = DigitalCyan
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "BMI: $bmiStr ($bmiCat)",
                        fontSize = 11.sp,
                        fontFamily = FontFamily.Monospace,
                        color = Color(0xFF7DD3FC)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!isSelected) {
                OutlinedButton(
                    onClick = onSelect,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = DigitalCyan),
                    border = androidx.compose.foundation.BorderStroke(1.dp, DigitalCyan.copy(alpha = 0.5f)),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth().height(38.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = "Pilih sebagai Karakter Aktif", fontSize = 12.sp)
                }
            }
        }
    }
}
