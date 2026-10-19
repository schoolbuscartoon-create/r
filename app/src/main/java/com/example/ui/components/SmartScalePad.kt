package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TouchApp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

@Composable
fun SmartScalePad(
    weightDigitalString: String,
    bmiDigitalString: String,
    bmiCategory: String,
    isMeasuring: Boolean,
    onStepOnScale: () -> Unit,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "scalePulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isMeasuring) 1.03f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse"
    )

    // Tempered Glass Scale Body
    Box(
        modifier = modifier
            .fillMaxWidth()
            .scale(if (isMeasuring) pulseScale else 1f)
            .shadow(16.dp, RoundedCornerShape(28.dp), ambientColor = Color.Black, spotColor = DigitalCyan)
            .clip(RoundedCornerShape(28.dp))
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF19253B),
                        Color(0xFF10192A),
                        Color(0xFF0C1322)
                    )
                )
            )
            .border(
                width = 2.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF38517A),
                        Color(0xFF1C2C45)
                    )
                ),
                shape = RoundedCornerShape(28.dp)
            )
            .clickable { onStepOnScale() }
            .testTag("scale_pad_surface")
            .padding(16.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // LCD Display Module on top center of scale
            DigitalLcdDisplay(
                valueString = weightDigitalString,
                unitString = "kg",
                subLabel = "BMI: $bmiDigitalString ($bmiCategory)",
                isMeasuring = isMeasuring,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp)
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Scale Platform with 4 Stainless Steel Electrodes (Bio-impedance simulation)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            ) {
                // Top-Left electrode
                ElectrodeSensor(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(start = 12.dp, top = 4.dp)
                )

                // Top-Right electrode
                ElectrodeSensor(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(end = 12.dp, top = 4.dp)
                )

                // Bottom-Left electrode
                ElectrodeSensor(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 12.dp, bottom = 4.dp)
                )

                // Bottom-Right electrode
                ElectrodeSensor(
                    modifier = Modifier
                        .align(Alignment.BottomEnd)
                        .padding(end = 12.dp, bottom = 4.dp)
                )

                // Center Action / Status
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(
                                if (isMeasuring) DigitalCyanGlow else Color(0x1A00E5FF)
                            )
                            .border(1.dp, DigitalCyan.copy(alpha = 0.5f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.TouchApp,
                            contentDescription = "Injak Timbangan",
                            tint = DigitalCyan,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isMeasuring) "Mengukur..." else "Sentuh / Injak Timbangan",
                        color = Color(0xFFBACADB),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun ElectrodeSensor(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .size(52.dp)
            .clip(CircleShape)
            .background(
                brush = Brush.radialGradient(
                    colors = listOf(
                        Color(0xFFE2E8F0),
                        Color(0xFF94A3B8),
                        Color(0xFF475569)
                    )
                )
            )
            .border(1.5.dp, Color(0xFFCBD5E1), CircleShape)
    )
}
