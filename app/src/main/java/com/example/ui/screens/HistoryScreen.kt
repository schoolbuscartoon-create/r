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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.data.model.ScaleRecord
import com.example.data.model.ScaleUtils
import com.example.ui.ScaleViewModel
import com.example.ui.theme.BmiNormal
import com.example.ui.theme.BmiObese
import com.example.ui.theme.BmiOverweight
import com.example.ui.theme.BmiUnderweight
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

@Composable
fun HistoryScreen(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    val records by viewModel.records.collectAsState()
    val characters by viewModel.characters.collectAsState()
    val filterCharacterId by viewModel.historyFilterCharacterId.collectAsState()
    var recordToDelete by remember { mutableStateOf<ScaleRecord?>(null) }

    val filteredRecords = if (filterCharacterId == null) {
        records
    } else {
        records.filter { it.characterId == filterCharacterId }
    }

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
                            imageVector = Icons.Default.History,
                            contentDescription = null,
                            tint = DigitalCyan,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Riwayat Timbangan",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Text(
                        text = "${filteredRecords.size} Catatan Timbangan Tersimpan",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                }
            }

            // Character Filter Chips
            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    val isAllSelected = filterCharacterId == null
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isAllSelected) DigitalCyanGlow else ScaleNavyCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isAllSelected) DigitalCyan else ScaleNavyBorder
                        ),
                        modifier = Modifier
                            .clickable { viewModel.setHistoryFilter(null) }
                            .testTag("filter_all_characters")
                    ) {
                        Text(
                            text = "Semua Karakter",
                            fontSize = 11.sp,
                            fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isAllSelected) DigitalCyan else Color(0xFFCBD5E1),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }

                items(characters, key = { it.id }) { char ->
                    val isCharSelected = filterCharacterId == char.id
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = if (isCharSelected) DigitalCyanGlow else ScaleNavyCard,
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isCharSelected) DigitalCyan else ScaleNavyBorder
                        ),
                        modifier = Modifier
                            .clickable { viewModel.setHistoryFilter(char.id) }
                            .testTag("filter_character_${char.id}")
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text(text = char.avatarEmoji, fontSize = 12.sp)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = char.name,
                                fontSize = 11.sp,
                                fontWeight = if (isCharSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isCharSelected) DigitalCyan else Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (filteredRecords.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 80.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Belum Ada Riwayat Timbangan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF94A3B8)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Buka tab 'Timbang' lalu tekan 'Simpan Hasil Timbangan'",
                            fontSize = 12.sp,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 100.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    items(filteredRecords, key = { it.id }) { record ->
                        RecordCard(
                            record = record,
                            onDelete = { recordToDelete = record },
                            onPhotoClick = { record.photoUri?.let { viewModel.openPhotoPreview(it) } }
                        )
                    }
                }
            }
        }

        // Delete confirmation
        recordToDelete?.let { rec ->
            AlertDialog(
                onDismissRequest = { recordToDelete = null },
                title = { Text("Hapus Catatan Ini?") },
                text = { Text("Catatan timbangan tanggal ${rec.dateStr} (${rec.weightKg} kg) akan dihapus.") },
                confirmButton = {
                    Button(
                        onClick = {
                            viewModel.deleteRecord(rec)
                            recordToDelete = null
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFF43F5E))
                    ) {
                        Text("Hapus")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { recordToDelete = null }) {
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
private fun RecordCard(
    record: ScaleRecord,
    onDelete: () -> Unit,
    onPhotoClick: () -> Unit
) {
    val weightStr = ScaleUtils.formatWeightDigital(record.weightKg)
    val heightStr = ScaleUtils.formatHeight(record.heightCm)
    val bmiStr = ScaleUtils.formatBmiDigital(record.bmi)

    val bmiColor = when (record.bmiCategory) {
        "Kurus" -> BmiUnderweight
        "Normal / Ideal" -> BmiNormal
        "Kelebihan Berat" -> BmiOverweight
        else -> BmiObese
    }

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ScaleNavyCard,
        border = androidx.compose.foundation.BorderStroke(1.dp, ScaleNavyBorder),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("record_card_${record.id}")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Row 1: Tgl & Jam Waktu and Character Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Tgl: ${record.dateStr}",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color.White
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = Color(0xFF94A3B8),
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Jam: ${record.timeStr}",
                        fontSize = 11.sp,
                        color = Color(0xFFCBD5E1)
                    )
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.DeleteOutline,
                        contentDescription = "Hapus",
                        tint = Color(0xFFF43F5E),
                        modifier = Modifier.size(16.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Character info badge
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = record.characterName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(DigitalCyanGlow)
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = "Peran: ${record.characterRole}",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium,
                        color = DigitalCyan
                    )
                }
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "${record.gender} • ${record.age} thn",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Row 3: Weight, Height, BMI Readout
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF0C1625))
                    .border(1.dp, Color(0xFF1E2D44), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Berat 000000.00",
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = "$weightStr kg",
                        fontSize = 15.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = DigitalCyan
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Tinggi .0 cm",
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Text(
                        text = heightStr,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "BMI 000000.00",
                        fontSize = 9.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = bmiStr,
                            fontSize = 13.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = bmiColor
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(bmiColor.copy(alpha = 0.2f))
                                .padding(horizontal = 4.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = record.bmiCategory,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = bmiColor
                            )
                        }
                    }
                }
            }

            // Attached Photo & Notes if available
            if (!record.photoUri.isNullOrBlank() || record.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (!record.photoUri.isNullOrBlank()) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .border(1.dp, DigitalCyan, RoundedCornerShape(8.dp))
                                .clickable { onPhotoClick() }
                        ) {
                            AsyncImage(
                                model = record.photoUri,
                                contentDescription = "Foto Timbangan Terlampir",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0x99000000))
                                    .padding(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                    }

                    if (record.notes.isNotBlank()) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Catatan:",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                            Text(
                                text = record.notes,
                                fontSize = 12.sp,
                                color = Color(0xFFCBD5E1)
                            )
                        }
                    }
                }
            }
        }
    }
}
