package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MonitorWeight
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.ScaleViewModel
import com.example.ui.components.CharacterDialog
import com.example.ui.components.PhotoPreviewDialog
import com.example.ui.screens.CharactersScreen
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.MeasureScreen
import com.example.ui.theme.DigitalCyan
import com.example.ui.theme.DigitalCyanGlow
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.theme.ScaleNavyBorder
import com.example.ui.theme.ScaleNavyCard
import com.example.ui.theme.ScaleNavyDeep

class MainActivity : ComponentActivity() {

    private val viewModel: ScaleViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                MainAppScreen(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun MainAppScreen(viewModel: ScaleViewModel) {
    var selectedTab by rememberSaveable { mutableIntStateOf(0) }

    val showCharacterDialog by viewModel.showCharacterDialog.collectAsState()
    val editingCharacter by viewModel.editingCharacter.collectAsState()
    val previewPhotoUri by viewModel.previewPhotoUri.collectAsState()

    // Back handling for tabs
    if (selectedTab != 0) {
        BackHandler {
            selectedTab = 0
        }
    }

    Scaffold(
        contentWindowInsets = WindowInsets.safeDrawing,
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                containerColor = ScaleNavyCard,
                contentColor = Color.White,
                tonalElevation = 8.dp,
                windowInsets = WindowInsets.navigationBars,
                modifier = Modifier
                    .background(ScaleNavyDeep)
                    .testTag("main_bottom_nav")
            ) {
                // Tab 0: Timbang
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 0) Icons.Filled.MonitorWeight else Icons.Outlined.MonitorWeight,
                            contentDescription = "Timbang"
                        )
                    },
                    label = { Text("Timbang", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF04101A),
                        selectedTextColor = DigitalCyan,
                        indicatorColor = DigitalCyan,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_measure")
                )

                // Tab 1: Karakter
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 1) Icons.Filled.Group else Icons.Outlined.Group,
                            contentDescription = "Karakter"
                        )
                    },
                    label = { Text("Karakter", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF04101A),
                        selectedTextColor = DigitalCyan,
                        indicatorColor = DigitalCyan,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_characters")
                )

                // Tab 2: Riwayat
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = {
                        Icon(
                            imageVector = if (selectedTab == 2) Icons.Filled.History else Icons.Outlined.History,
                            contentDescription = "Riwayat"
                        )
                    },
                    label = { Text("Riwayat", fontSize = 11.sp) },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = Color(0xFF04101A),
                        selectedTextColor = DigitalCyan,
                        indicatorColor = DigitalCyan,
                        unselectedIconColor = Color(0xFF94A3B8),
                        unselectedTextColor = Color(0xFF94A3B8)
                    ),
                    modifier = Modifier.testTag("nav_tab_history")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (selectedTab) {
                0 -> MeasureScreen(viewModel = viewModel)
                1 -> CharactersScreen(viewModel = viewModel)
                2 -> HistoryScreen(viewModel = viewModel)
            }
        }
    }

    // Modal Character Dialog (Add / Edit)
    if (showCharacterDialog) {
        CharacterDialog(
            character = editingCharacter,
            onDismiss = { viewModel.closeCharacterDialog() },
            onSave = { name, role, gender, dob, height, weight, avatar, photo ->
                viewModel.saveCharacter(name, role, gender, dob, height, weight, avatar, photo)
            }
        )
    }

    // Modal Photo Preview Dialog
    if (previewPhotoUri != null) {
        PhotoPreviewDialog(
            photoUri = previewPhotoUri,
            onDismiss = { viewModel.closePhotoPreview() }
        )
    }
}
