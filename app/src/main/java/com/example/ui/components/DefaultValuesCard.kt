package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyCharacter
import com.example.data.model.ScaleUtils
import com.example.ui.theme.BmiNormal
import com.example.ui.theme.BmiObese
import com.example.ui.theme.BmiOverweight
import com.example.ui.theme.BmiUnderweight
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DefaultValuesCard(
    character: FamilyCharacter?,
    currentWeight: Double,
    currentHeight: Double,
    modifier: Modifier = Modifier
) {
    val charName = character?.name ?: "Pengguna"
    val role = character?.role ?: "Pribadi"
    val gender = character?.gender ?: "Laki-laki"
    val dob = character?.dob ?: "15-Jan-1985"
    val age = character?.age ?: ScaleUtils.calculateAgeFromDob(dob)
    val weightStr = ScaleUtils.formatWeightDigital(currentWeight)
    val heightStr = ScaleUtils.formatHeight(currentHeight)
    val bmi = ScaleUtils.calculateBmi(currentWeight, currentHeight)
    val bmiStr = ScaleUtils.formatBmiDigital(bmi)
    val bmiCat = ScaleUtils.getBmiCategory(bmi)

    val bmiColor = when (bmiCat) {
        "Kurus" -> BmiUnderweight
        "Normal / Ideal" -> BmiNormal
        "Kelebihan Berat" -> BmiOverweight
        else -> BmiObese
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        ScaleNavyCard,
                        Color(0xFF0F1A2C)
                    )
                )
            )
            .border(1.dp, ScaleNavyBorder, RoundedCornerShape(20.dp))
            .padding(16.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(DigitalCyan)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Profil Parameter Default",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = charName,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DigitalCyan
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Grid of attributes
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                // Row 1: Peran Keluarga & Gender
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AttributeItem(
                        label = "Peran Keluarga (default)",
                        value = role,
                        icon = Icons.Default.Badge,
                        modifier = Modifier.weight(1f)
                    )
                    AttributeItem(
                        label = "Gender (default)",
                        value = gender,
                        icon = Icons.Default.Person,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 2: DOB & Timbangan Usia
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AttributeItem(
                        label = "DOB HH-BBB-TTTT (default)",
                        value = dob,
                        icon = Icons.Default.CalendarMonth,
                        modifier = Modifier.weight(1f)
                    )
                    AttributeItem(
                        label = "Timbangan Usia (default)",
                        value = "$age Tahun",
                        icon = Icons.Default.FitnessCenter,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 3: Tinggi & Berat
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    AttributeItem(
                        label = "Tinggi .0 cm (default)",
                        value = heightStr,
                        icon = Icons.Default.Height,
                        modifier = Modifier.weight(1f)
                    )
                    AttributeItem(
                        label = "Berat 000000.00 (default)",
                        value = "$weightStr kg",
                        icon = Icons.Default.MonitorWeight,
                        highlightColor = DigitalCyan,
                        isMonospace = true,
                        modifier = Modifier.weight(1f)
                    )
                }

                // Row 4: BMI Full Card
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0A1322))
                        .border(1.dp, bmiColor.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Speed,
                                contentDescription = "BMI",
                                tint = bmiColor,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "BMI 000000.00 (default)",
                                    fontSize = 11.sp,
                                    color = Color(0xFF94A3B8)
                                )
                                Text(
                                    text = bmiStr,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold,
                                    fontFamily = FontFamily.Monospace,
                                    color = bmiColor
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(bmiColor.copy(alpha = 0.15f))
                                .border(1.dp, bmiColor, RoundedCornerShape(8.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = bmiCat,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = bmiColor
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttributeItem(
    label: String,
    value: String,
    icon: ImageVector,
    modifier: Modifier = Modifier,
    highlightColor: Color? = null,
    isMonospace: Boolean = false
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(Color(0xFF0D1726))
            .border(1.dp, Color(0xFF1E2D44), RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color(0xFF64748B),
                    modifier = Modifier.size(13.dp)
                )
                Spacer(modifier = Modifier.width(5.dp))
                Text(
                    text = label,
                    fontSize = 10.sp,
                    color = Color(0xFF94A3B8),
                    maxLines = 1
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                fontSize = if (isMonospace) 13.sp else 13.sp,
                fontWeight = FontWeight.SemiBold,
                fontFamily = if (isMonospace) FontFamily.Monospace else FontFamily.Default,
                color = highlightColor ?: Color.White
            )
        }
    }
}
