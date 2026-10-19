package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ScaleUtils
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard

@Composable
fun ScaleControls(
    currentWeight: Double,
    currentHeight: Double,
    currentDate: String,
    currentTime: String,
    onAdjustWeight: (Double) -> Unit,
    onSetWeight: (Double) -> Unit,
    onSetHeight: (Double) -> Unit,
    onRefreshDateTime: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ScaleNavyCard)
            .border(1.dp, ScaleNavyBorder, RoundedCornerShape(20.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Tgl & Jam Waktu Header Bar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFF0C1625))
                .border(1.dp, Color(0xFF1E2D44), RoundedCornerShape(12.dp))
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tgl:
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = "Tanggal",
                        tint = DigitalCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Tgl:",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = currentDate,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                // Jam Waktu:
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = "Jam Waktu",
                        tint = DigitalCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "Jam Waktu:",
                            fontSize = 10.sp,
                            color = Color(0xFF94A3B8)
                        )
                        Text(
                            text = currentTime,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }
            }

            IconButton(
                onClick = onRefreshDateTime,
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(DigitalCyanGlow)
                    .testTag("refresh_date_time_button")
            ) {
                Icon(
                    imageVector = Icons.Default.Refresh,
                    contentDescription = "Perbarui Waktu",
                    tint = DigitalCyan,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Quick Weight Adjustment Stepper
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = null,
                        tint = DigitalCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Penyesuaian Berat Cepat",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFCBD5E1)
                    )
                }

                Text(
                    text = "${ScaleUtils.formatWeightShort(currentWeight)} kg",
                    fontSize = 14.sp,
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
                QuickStepButton(label = "-1.0", onClick = { onAdjustWeight(-1.0) }, modifier = Modifier.weight(1f))
                QuickStepButton(label = "-0.5", onClick = { onAdjustWeight(-0.5) }, modifier = Modifier.weight(1f))
                QuickStepButton(label = "-0.1", onClick = { onAdjustWeight(-0.1) }, modifier = Modifier.weight(1f))
                QuickStepButton(label = "+0.1", onClick = { onAdjustWeight(+0.1) }, isPositive = true, modifier = Modifier.weight(1f))
                QuickStepButton(label = "+0.5", onClick = { onAdjustWeight(+0.5) }, isPositive = true, modifier = Modifier.weight(1f))
                QuickStepButton(label = "+1.0", onClick = { onAdjustWeight(+1.0) }, isPositive = true, modifier = Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Slider for broad range
            Slider(
                value = currentWeight.toFloat(),
                onValueChange = { onSetWeight(it.toDouble()) },
                valueRange = 10f..180f,
                colors = SliderDefaults.colors(
                    thumbColor = DigitalCyan,
                    activeTrackColor = DigitalCyan,
                    inactiveTrackColor = Color(0xFF1E2D44)
                ),
                modifier = Modifier.fillMaxWidth().testTag("weight_slider")
            )
        }

        // Height Adjustment Stepper
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Height,
                        contentDescription = null,
                        tint = DigitalCyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Tinggi Badan (.0 cm)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFFCBD5E1)
                    )
                }

                Text(
                    text = ScaleUtils.formatHeight(currentHeight),
                    fontSize = 14.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Slider(
                value = currentHeight.toFloat(),
                onValueChange = { onSetHeight(it.toDouble()) },
                valueRange = 80f..220f,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFF38BDF8),
                    activeTrackColor = Color(0xFF38BDF8),
                    inactiveTrackColor = Color(0xFF1E2D44)
                ),
                modifier = Modifier.fillMaxWidth().testTag("height_slider")
            )
        }
    }
}

@Composable
private fun QuickStepButton(
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPositive: Boolean = false
) {
    Surface(
        shape = RoundedCornerShape(8.dp),
        color = if (isPositive) Color(0x2200E5FF) else Color(0xFF131F33),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isPositive) DigitalCyan.copy(alpha = 0.5f) else Color(0xFF263750)
        ),
        modifier = modifier
            .height(34.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold,
                color = if (isPositive) DigitalCyan else Color(0xFFCBD5E1)
            )
        }
    }
}
