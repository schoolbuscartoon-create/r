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
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard

@Composable
fun PhotoUploadSection(
    uploadedPhotoUri: String?,
    onPhotoSelected: (String?) -> Unit,
    onPhotoClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Zero-permission Android Photo Picker contract
    val photoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia()
    ) { uri: Uri? ->
        if (uri != null) {
            onPhotoSelected(uri.toString())
        }
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(ScaleNavyCard)
            .border(1.dp, ScaleNavyBorder, RoundedCornerShape(18.dp))
            .padding(14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera,
                        contentDescription = "Upload Photo",
                        tint = DigitalCyan,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Upload Photo",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }

                Text(
                    text = if (uploadedPhotoUri != null) "Foto Terlampir" else "Opsional",
                    fontSize = 11.sp,
                    color = if (uploadedPhotoUri != null) DigitalCyan else Color(0xFF94A3B8)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            if (!uploadedPhotoUri.isNullOrBlank()) {
                // Photo attached preview
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clickable { onPhotoClick(uploadedPhotoUri) }
                            .testTag("preview_photo_thumbnail")
                    ) {
                        Box(
                            modifier = Modifier
                                .size(64.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .border(1.5.dp, DigitalCyan, RoundedCornerShape(10.dp))
                        ) {
                            AsyncImage(
                                model = uploadedPhotoUri,
                                contentDescription = "Foto Timbangan Terlampir",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxWidth().height(64.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .background(Color(0xAA000000), RoundedCornerShape(topStart = 6.dp))
                                    .padding(2.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.ZoomIn,
                                    contentDescription = "Perbesar",
                                    tint = Color.White,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Text(
                                text = "Foto Layar / Progres",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Ketuk untuk melihat foto penuh",
                                fontSize = 11.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Row {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = Color(0x2200E5FF),
                            border = androidx.compose.foundation.BorderStroke(1.dp, DigitalCyan.copy(alpha = 0.5f)),
                            modifier = Modifier
                                .clickable {
                                    photoPickerLauncher.launch(
                                        PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                    )
                                }
                                .padding(horizontal = 8.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "Ganti",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                color = DigitalCyan
                            )
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        IconButton(
                            onClick = { onPhotoSelected(null) },
                            modifier = Modifier
                                .size(32.dp)
                                .clip(CircleShape)
                                .background(Color(0x33F43F5E))
                                .testTag("remove_photo_button")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus Foto",
                                tint = Color(0xFFF43F5E),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            } else {
                // Empty photo placeholder with picker action
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color(0xFF0D1726))
                        .border(
                            width = 1.dp,
                            color = Color(0xFF1E2D44),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            photoPickerLauncher.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }
                        .testTag("upload_photo_button")
                        .padding(14.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(DigitalCyanGlow),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AddPhotoAlternate,
                                contentDescription = "Pilih Foto",
                                tint = DigitalCyan,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = "Upload Photo Layar Timbangan / Progres",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.White
                            )
                            Text(
                                text = "Pilih gambar dari galeri perangkat",
                                fontSize = 10.sp,
                                color = Color(0xFF94A3B8)
                            )
                        }
                    }
                }
            }
        }
    }
}
