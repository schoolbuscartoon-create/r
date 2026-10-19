package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanDark
import com.example.ui.theme.DigitalLedOff
import com.example.ui.theme.ScaleNavyDeep

@Composable
fun DigitalLcdDisplay(
    valueString: String, // e.g. "000072.80"
    unitString: String,  // e.g. "kg"
    subLabel: String? = null, // e.g. "BMI: 000024.61 (Ideal)"
    isMeasuring: Boolean = false,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF040810),
                        Color(0xFF091322),
                        Color(0xFF040810)
                    )
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF2A3D5D),
                        Color(0xFF142035)
                    )
                ),
                shape = RoundedCornerShape(12.dp)
            )
            .padding(horizontal = 16.dp, vertical = 10.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Status bar inside LCD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .width(8.dp)
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp))
                            .background(if (isMeasuring) Color(0xFFFFB703) else DigitalCyan)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isMeasuring) "TIMBANG..." else "STABIL",
                        color = if (isMeasuring) Color(0xFFFFB703) else DigitalCyan,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        fontFamily = FontFamily.Monospace,
                        letterSpacing = 1.sp
                    )
                }

                Text(
                    text = "DIGITAL SCALE 000000.00",
                    color = Color(0xFF4A6588),
                    fontSize = 9.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            // Main 7-segment / Monospace Digital readout
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Dim unlit segments background simulation
                Text(
                    text = "888888.88",
                    color = DigitalLedOff,
                    fontSize = 38.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 2.sp
                )

                // Glowing Active Digits
                Row(
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = valueString,
                        color = DigitalCyan,
                        fontSize = 38.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 2.sp
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = unitString,
                        color = DigitalCyanDark,
                        fontSize = 18.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            if (subLabel != null) {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = subLabel,
                    color = Color(0xFF7CA3D4),
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
