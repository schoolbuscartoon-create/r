package com.example.ui.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleUtils
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun CharacterDialog(
    character: FamilyCharacter?,
    onDismiss: () -> Unit,
    onSave: (
        name: String,
        role: String,
        gender: String,
        dob: String,
        heightCm: Double,
        defaultWeightKg: Double,
        avatarEmoji: String,
        photoUri: String?
    ) -> Unit
) {
    val roles = listOf("Ayah", "Ibu", "Anak", "Kakek", "Nenek", "Saudara", "Pribadi")
    val genders = listOf("Laki-laki", "Perempuan")
    val presetEmojis = listOf("👨", "👩", "👦", "👧", "👴", "👵", "🧑", "🏋️", "🧘", "🏃", "👸", "🦸")

    var name by remember { mutableStateOf(character?.name ?: "") }
    var selectedRole by remember { mutableStateOf(character?.role ?: "Ayah") }
    var selectedGender by remember { mutableStateOf(character?.gender ?: "Laki-laki") }
    var avatarEmoji by remember { mutableStateOf(character?.avatarEmoji ?: "👨") }
    var photoUri by remember { mutableStateOf<String?>(character?.photoUri) }

    // DOB Day, Month, Year
    var day by remember {
        val initialDay = character?.dob?.split("-")?.getOrNull(0)?.toIntOrNull() ?: 15
        mutableIntStateOf(initialDay)
    }
    var monthIdx by remember {
        val monthStr = character?.dob?.split("-")?.getOrNull(1) ?: "Jan"
        val idx = ScaleUtils.getIndonesianMonths().indexOf(monthStr).coerceAtLeast(0)
        mutableIntStateOf(idx)
    }
    var year by remember {
        val initialYear = character?.dob?.split("-")?.getOrNull(2)?.toIntOrNull() ?: 1990
        mutableIntStateOf(initialYear)
    }

    var heightCm by remember { mutableDoubleStateOf(character?.heightCm ?: 170.0) }
    var defaultWeightKg by remember { mutableDoubleStateOf(character?.defaultWeightKg ?: 65.0) }

    val formattedDob = ScaleUtils.formatDob(day, monthIdx, year)
    val calculatedAge = ScaleUtils.calculateAgeFromDob(formattedDob)
    val calculatedBmi = ScaleUtils.calculateBmi(defaultWeightKg, heightCm)

    // Photo picker
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            photoUri = uri.toString()
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = ScaleNavyDeep,
            border = androidx.compose.foundation.BorderStroke(1.dp, ScaleNavyBorder),
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (character != null) "Edit Karakter" else "Tambah Karakter",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = onDismiss,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Close,
                            contentDescription = "Tutup",
                            tint = Color(0xFF94A3B8)
                        )
                    }
                }

                // Avatar / Photo section
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CharacterAvatar(
                        avatarEmoji = avatarEmoji,
                        photoUri = photoUri,
                        size = 64.dp,
                        isSelected = true
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    Column {
                        OutlinedButton(
                            onClick = {
                                photoPickerLauncher.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = DigitalCyan),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DigitalCyan),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "Upload Foto", fontSize = 12.sp)
                        }

                        if (photoUri != null) {
                            Text(
                                text = "Hapus foto",
                                fontSize = 11.sp,
                                color = Color(0xFFF43F5E),
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .clickable { photoUri = null }
                            )
                        }
                    }
                }

                // Preset Emoji selector
                Column {
                    Text(
                        text = "Atau Pilih Emoji Karakter:",
                        fontSize = 11.sp,
                        color = Color(0xFF94A3B8)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        presetEmojis.forEach { emoji ->
                            val isChosen = avatarEmoji == emoji && photoUri == null
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(if (isChosen) DigitalCyanGlow else ScaleNavyCard)
                                    .border(
                                        width = if (isChosen) 2.dp else 1.dp,
                                        color = if (isChosen) DigitalCyan else ScaleNavyBorder,
                                        shape = CircleShape
                                    )
                                    .clickable {
                                        avatarEmoji = emoji
                                        photoUri = null
                                    },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(text = emoji, fontSize = 18.sp)
                            }
                        }
                    }
                }

                // Name TextField
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nama Lengkap / Panggilan") },
                    placeholder = { Text("contoh: Ayah Budi, Kakak Rian") },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = DigitalCyan,
                        unfocusedBorderColor = ScaleNavyBorder,
                        focusedLabelColor = DigitalCyan,
                        unfocusedLabelColor = Color(0xFF94A3B8),
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("character_name_input")
                )

                // Peran Keluarga (default)
                Column {
                    Text(
                        text = "Peran Keluarga (default):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        roles.forEach { role ->
                            val isSel = selectedRole == role
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSel) DigitalCyanGlow else ScaleNavyCard,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) DigitalCyan else ScaleNavyBorder
                                ),
                                modifier = Modifier.clickable { selectedRole = role }
                            ) {
                                Text(
                                    text = role,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = if (isSel) DigitalCyan else Color(0xFFCBD5E1),
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Gender (default)
                Column {
                    Text(
                        text = "Gender (default):",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFCBD5E1)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        genders.forEach { g ->
                            val isSel = selectedGender == g
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSel) DigitalCyanGlow else ScaleNavyCard,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSel) DigitalCyan else ScaleNavyBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedGender = g }
                            ) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                ) {
                                    Text(
                                        text = g,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSel) DigitalCyan else Color.White
                                    )
                                }
                            }
                        }
                    }
                }

                // DOB HH-BBB-TTTT default & Timbangan Usia
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .background(ScaleNavyCard)
                        .border(1.dp, ScaleNavyBorder, RoundedCornerShape(14.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "DOB HH-BBB-TTTT (default)",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Text(
                            text = formattedDob,
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = DigitalCyan
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        // Day stepper
                        StepperMini(
                            label = "Hari (HH)",
                            value = String.format("%02d", day),
                            onMinus = { if (day > 1) day-- },
                            onPlus = { if (day < 31) day++ },
                            modifier = Modifier.weight(1f)
                        )

                        // Month stepper
                        StepperMini(
                            label = "Bulan (BBB)",
                            value = ScaleUtils.getIndonesianMonths()[monthIdx],
                            onMinus = { if (monthIdx > 0) monthIdx-- },
                            onPlus = { if (monthIdx < 11) monthIdx++ },
                            modifier = Modifier.weight(1f)
                        )

                        // Year stepper
                        StepperMini(
                            label = "Tahun (TTTT)",
                            value = year.toString(),
                            onMinus = { if (year > 1920) year-- },
                            onPlus = { if (year < 2026) year++ },
                            modifier = Modifier.weight(1.2f)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Timbangan Usia (default) derived
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Timbangan Usia (default):",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "$calculatedAge Tahun",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF10B981)
                        )
                    }
                }

                // Tinggi .0 cm (default) & Berat 000000.00 (default)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StepperMini(
                        label = "Tinggi .0 cm (default)",
                        value = ScaleUtils.formatHeight(heightCm),
                        onMinus = { heightCm = (heightCm - 1.0).coerceAtLeast(50.0) },
                        onPlus = { heightCm = (heightCm + 1.0).coerceAtMost(240.0) },
                        modifier = Modifier.weight(1f)
                    )

                    StepperMini(
                        label = "Berat 000000.00 (default)",
                        value = "${ScaleUtils.formatWeightShort(defaultWeightKg)} kg",
                        onMinus = { defaultWeightKg = (defaultWeightKg - 0.5).coerceAtLeast(5.0) },
                        onPlus = { defaultWeightKg = (defaultWeightKg + 0.5).coerceAtMost(250.0) },
                        modifier = Modifier.weight(1f)
                    )
                }

                // BMI preview
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF091322))
                        .border(1.dp, Color(0xFF1C2C45), RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "BMI 000000.00 (default):",
                            fontSize = 11.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = "${ScaleUtils.formatBmiDigital(calculatedBmi)} (${ScaleUtils.getBmiCategory(calculatedBmi)})",
                            fontSize = 12.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = DigitalCyan
                        )
                    }
                }

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF94A3B8)),
                        border = androidx.compose.foundation.BorderStroke(1.dp, ScaleNavyBorder),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Batal")
                    }

                    Button(
                        onClick = {
                            val finalName = name.ifBlank { "$selectedRole Baru" }
                            onSave(
                                finalName,
                                selectedRole,
                                selectedGender,
                                formattedDob,
                                heightCm,
                                defaultWeightKg,
                                avatarEmoji,
                                photoUri
                            )
                        },
                        modifier = Modifier.weight(1.5f).testTag("save_character_button"),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DigitalCyan,
                            contentColor = Color(0xFF04101A)
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = if (character != null) "Perbarui" else "Simpan Karakter",
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun StepperMini(
    label: String,
    value: String,
    onMinus: () -> Unit,
    onPlus: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(10.dp))
            .background(Color(0xFF0F1A2A))
            .border(1.dp, Color(0xFF1E2D44), RoundedCornerShape(10.dp))
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = label,
            fontSize = 9.sp,
            color = Color(0xFF94A3B8),
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = value,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(4.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Surface(
                shape = CircleShape,
                color = Color(0xFF1E2E48),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onMinus() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "−", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
            Surface(
                shape = CircleShape,
                color = Color(0xFF1E2E48),
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onPlus() }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(text = "+", color = DigitalCyan, fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
