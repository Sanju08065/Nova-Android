package com.nova.assistant.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.nova.assistant.viewmodel.NovaUiState

@Composable
fun SettingsScreen(
    uiState: NovaUiState,
    onToggleWakeWord: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Settings",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            )
        }
        
        item {
            Spacer(modifier = Modifier.height(8.dp))
        }
        
        item {
            SettingsSection(title = "Voice Recognition") {
                SettingItem(
                    icon = Icons.Default.RecordVoiceOver,
                    title = "Wake Word Detection",
                    subtitle = "Enable \"Hey Nova\" wake word",
                    trailing = {
                        Switch(
                            checked = uiState.wakeWordEnabled,
                            onCheckedChange = { onToggleWakeWord() },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF58A6FF),
                                uncheckedThumbColor = Color.Gray,
                                uncheckedTrackColor = Color(0xFF21262D)
                            )
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.Mic,
                    title = "Speech Recognition",
                    subtitle = "Offline speech-to-text engine",
                    trailing = {
                        Text(
                            text = "Android STT",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray
                            )
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.VolumeUp,
                    title = "Text-to-Speech",
                    subtitle = "Voice response settings",
                    trailing = {
                        Text(
                            text = "Android TTS",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray
                            )
                        )
                    }
                )
            }
        }
        
        item {
            SettingsSection(title = "Commands") {
                SettingItem(
                    icon = Icons.Default.List,
                    title = "Available Commands",
                    subtitle = "150+ built-in commands",
                    trailing = {
                        Text(
                            text = "View All",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFF58A6FF)
                            )
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.Speed,
                    title = "Response Speed",
                    subtitle = "Command execution speed",
                    trailing = {
                        Text(
                            text = "Fast",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color.Gray
                            )
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.Security,
                    title = "Permissions",
                    subtitle = "Manage app permissions",
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Open permissions",
                            tint = Color.Gray
                        )
                    }
                )
            }
        }
        
        item {
            SettingsSection(title = "Privacy & Security") {
                SettingItem(
                    icon = Icons.Default.CloudOff,
                    title = "Offline Mode",
                    subtitle = "All processing done locally",
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Enabled",
                            tint = Color(0xFF58A6FF)
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.Storage,
                    title = "Local Data Only",
                    subtitle = "No data sent to servers",
                    trailing = {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Enabled",
                            tint = Color(0xFF58A6FF)
                        )
                    }
                )
                
                SettingItem(
                    icon = Icons.Default.DeleteSweep,
                    title = "Clear Voice Data",
                    subtitle = "Delete stored voice recordings",
                    trailing = {
                        Text(
                            text = "Clear",
                            style = MaterialTheme.typography.bodyMedium.copy(
                                color = Color(0xFFFF6B6B)
                            )
                        )
                    }
                )
            }
        }
        
        item {
            SettingsSection(title = "About") {
                SettingItem(
                    icon = Icons.Default.Info,
                    title = "Version",
                    subtitle = "NOVA Voice Assistant v1.0",
                    trailing = null
                )
                
                SettingItem(
                    icon = Icons.Default.Code,
                    title = "Open Source",
                    subtitle = "Built with Android & Kotlin",
                    trailing = null
                )
                
                SettingItem(
                    icon = Icons.Default.Favorite,
                    title = "Built with ❤️",
                    subtitle = "Offline-first voice assistant",
                    trailing = null
                )
            }
        }
        
        item {
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SettingsSection(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF21262D).copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF58A6FF)
                )
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            content()
        }
    }
}

@Composable
fun SettingItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    trailing: @Composable (() -> Unit)?
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = Color(0xFF58A6FF),
            modifier = Modifier.size(24.dp)
        )
        
        Spacer(modifier = Modifier.width(16.dp))
        
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            )
            
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                )
            )
        }
        
        if (trailing != null) {
            Spacer(modifier = Modifier.width(8.dp))
            trailing()
        }
    }
}