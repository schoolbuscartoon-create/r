package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScaleUtils
import com.example.ui.ScaleViewModel
import com.example.ui.components.CharacterSelectorBar
import com.example.ui.components.DefaultValuesCard
import com.example.ui.components.PhotoUploadSection
import com.example.ui.components.ScaleControls
import com.example.ui.components.SmartScalePad
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

@Composable
fun MeasureScreen(
    viewModel: ScaleViewModel,
    modifier: Modifier = Modifier
) {
    val characters by viewModel.characters.collectAsState()
    val selectedCharacter by viewModel.selectedCharacter.collectAsState()
    val currentWeight by viewModel.currentWeight.collectAsState()
    val currentHeight by viewModel.currentHeight.collectAsState()
    val currentDate by viewModel.currentDate.collectAsState()
    val currentTime by viewModel.currentTime.collectAsState()
    val uploadedPhotoUri by viewModel.uploadedPhotoUri.collectAsState()
    val notes by viewModel.notes.collectAsState()
    val isMeasuring by viewModel.isMeasuring.collectAsState()
    val statusMessage by viewModel.statusMessage.collectAsState()

    val weightDigitalStr = ScaleUtils.formatWeightDigital(currentWeight)
    val bmi = ScaleUtils.calculateBmi(currentWeight, currentHeight)
    val bmiDigitalStr = ScaleUtils.formatBmiDigital(bmi)
    val bmiCat = ScaleUtils.getBmiCategory(bmi)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(ScaleNavyDeep)
            .verticalScroll(rememberScrollState())
            .padding(bottom = 90.dp)
    ) {
        // App Header
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Column {
                Text(
                    text = "Timbangan Badan Digital",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )
                Text(
                    text = "Smart Scale & Family Profile Management",
                    fontSize = 11.sp,
                    color = Color(0xFF94A3B8)
                )
            }
        }

        // Status banner when saved
        AnimatedVisibility(
            visible = statusMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            statusMessage?.let { msg ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0xFF0F3B2A))
                        .border(1.dp, Color(0xFF10B981), RoundedCornerShape(10.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF10B981),
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = msg,
                            color = Color(0xFFD1FAE5),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        // 1. Karakter Selector Carousel
        CharacterSelectorBar(
            characters = characters,
            selectedCharacter = selectedCharacter,
            onSelectCharacter = { viewModel.selectCharacter(it) },
            onAddCharacterClick = { viewModel.openAddCharacterDialog() }
        )

        Spacer(modifier = Modifier.height(10.dp))

        // 2. Realistic Digital Smart Scale Pad
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            SmartScalePad(
                weightDigitalString = weightDigitalStr,
                bmiDigitalString = bmiDigitalStr,
                bmiCategory = bmiCat,
                isMeasuring = isMeasuring,
                onStepOnScale = { viewModel.simulateWeighing() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 3. Tgl, Jam Waktu & Live Controls
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            ScaleControls(
                currentWeight = currentWeight,
                currentHeight = currentHeight,
                currentDate = currentDate,
                currentTime = currentTime,
                onAdjustWeight = { viewModel.adjustWeight(it) },
                onSetWeight = { viewModel.setWeight(it) },
                onSetHeight = { viewModel.setHeight(it) },
                onRefreshDateTime = { viewModel.refreshDateTime() }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 4. Default Values Card (Requested: Timbangan Usia, Gender, BMI 000000.00, Peran Keluarga, Tinggi .0 cm, Berat 000000.00, DOB HH-BBB-TTTT)
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            DefaultValuesCard(
                character = selectedCharacter,
                currentWeight = currentWeight,
                currentHeight = currentHeight
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 5. Upload Photo Section
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            PhotoUploadSection(
                uploadedPhotoUri = uploadedPhotoUri,
                onPhotoSelected = { viewModel.setUploadedPhotoUri(it) },
                onPhotoClick = { viewModel.openPhotoPreview(it) }
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 6. Notes Input
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            OutlinedTextField(
                value = notes,
                onValueChange = { viewModel.setNotes(it) },
                label = { Text("Catatan Timbangan (Opsional)") },
                placeholder = { Text("contoh: Pagi sebelum sarapan, setelah olahraga...") },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = DigitalCyan,
                    unfocusedBorderColor = ScaleNavyBorder,
                    focusedLabelColor = DigitalCyan,
                    unfocusedLabelColor = Color(0xFF94A3B8),
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = ScaleNavyCard,
                    unfocusedContainerColor = ScaleNavyCard
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("scale_notes_input")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 7. Save Measurement Button
        Box(modifier = Modifier.padding(horizontal = 16.dp)) {
            Button(
                onClick = { viewModel.saveCurrentMeasurement() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = DigitalCyan,
                    contentColor = Color(0xFF04101A)
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .testTag("save_measurement_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Save,
                    contentDescription = null,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Simpan Hasil Timbangan",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
