package com.nova.assistant.ui.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nova.assistant.data.model.CommandCategory
import com.nova.assistant.ui.components.*
import com.nova.assistant.viewmodel.NovaViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: NovaViewModel) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val isListening by viewModel.isListening.collectAsStateWithLifecycle()
    val recognizedText by viewModel.recognizedText.collectAsStateWithLifecycle()
    val commands by viewModel.commands.collectAsStateWithLifecycle()
    val currentCategory by viewModel.currentCategory.collectAsStateWithLifecycle()
    
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF0D1117),
                        Color(0xFF161B22),
                        Color(0xFF21262D)
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Top App Bar
            NovaTopAppBar(
                isListening = isListening,
                wakeWordEnabled = uiState.wakeWordEnabled,
                onToggleWakeWord = { viewModel.toggleWakeWordDetection() }
            )
            
            // Status and Recognition Display
            StatusCard(
                statusMessage = uiState.statusMessage,
                recognizedText = recognizedText,
                isListening = isListening,
                isExecuting = uiState.isExecuting
            )
            
            // Tab Navigation
            TabRow(
                selectedTabIndex = selectedTabIndex,
                containerColor = Color.Transparent,
                contentColor = Color(0xFF58A6FF),
                indicator = { tabPositions ->
                    TabRowDefaults.Indicator(
                        modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        color = Color(0xFF58A6FF),
                        height = 3.dp
                    )
                }
            ) {
                Tab(
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 },
                    text = { 
                        Text("Voice", color = if (selectedTabIndex == 0) Color(0xFF58A6FF) else Color.Gray) 
                    }
                )
                Tab(
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 },
                    text = { 
                        Text("Commands", color = if (selectedTabIndex == 1) Color(0xFF58A6FF) else Color.Gray) 
                    }
                )
                Tab(
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 },
                    text = { 
                        Text("Settings", color = if (selectedTabIndex == 2) Color(0xFF58A6FF) else Color.Gray) 
                    }
                )
            }
            
            // Tab Content
            when (selectedTabIndex) {
                0 -> VoiceScreen(
                    isListening = isListening,
                    onStartListening = { viewModel.startListening() },
                    onStopListening = { viewModel.stopListening() },
                    modifier = Modifier.weight(1f)
                )
                1 -> CommandsScreen(
                    commands = commands,
                    currentCategory = currentCategory,
                    onCategorySelected = { viewModel.filterCommandsByCategory(it) },
                    onCommandExecute = { viewModel.executeCommand(it) },
                    onSearchCommands = { viewModel.searchCommands(it) },
                    modifier = Modifier.weight(1f)
                )
                2 -> SettingsScreen(
                    uiState = uiState,
                    onToggleWakeWord = { viewModel.toggleWakeWordDetection() },
                    modifier = Modifier.weight(1f)
                )
            }
        }
        
        // Floating Voice Button
        if (selectedTabIndex != 0) {
            FloatingVoiceButton(
                isListening = isListening,
                onStartListening = { viewModel.startListening() },
                onStopListening = { viewModel.stopListening() },
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp)
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NovaTopAppBar(
    isListening: Boolean,
    wakeWordEnabled: Boolean,
    onToggleWakeWord: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "NOVA",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF58A6FF)
                    )
                )
                
                Spacer(modifier = Modifier.width(8.dp))
                
                // Listening indicator
                if (isListening) {
                    PulsingDot()
                }
                
                Spacer(modifier = Modifier.weight(1f))
                
                // Wake word toggle
                IconButton(onClick = onToggleWakeWord) {
                    Icon(
                        imageVector = if (wakeWordEnabled) Icons.Default.Mic else Icons.Default.MicOff,
                        contentDescription = "Toggle wake word",
                        tint = if (wakeWordEnabled) Color(0xFF58A6FF) else Color.Gray
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent
        )
    )
}

@Composable
fun StatusCard(
    statusMessage: String,
    recognizedText: String,
    isListening: Boolean,
    isExecuting: Boolean
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF21262D).copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Status message
            Text(
                text = statusMessage,
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = when {
                        isListening -> Color(0xFF58A6FF)
                        isExecuting -> Color(0xFFFFB347)
                        else -> Color.White
                    }
                ),
                textAlign = TextAlign.Center
            )
            
            // Recognized text
            if (recognizedText.isNotEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "\"$recognizedText\"",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        color = Color(0xFF7C3AED),
                        fontWeight = FontWeight.Medium
                    ),
                    textAlign = TextAlign.Center
                )
            }
            
            // Loading indicator
            if (isExecuting) {
                Spacer(modifier = Modifier.height(12.dp))
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Color(0xFFFFB347)
                )
            }
        }
    }
}

@Composable
fun PulsingDot() {
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val scale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.5f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse
        ),
        label = "scale"
    )
    
    Box(
        modifier = Modifier
            .size(12.dp)
            .scale(scale)
            .clip(CircleShape)
            .background(Color(0xFF58A6FF))
    )
}

@Composable
fun FloatingVoiceButton(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isListening) 1.2f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    
    FloatingActionButton(
        onClick = {
            if (isListening) onStopListening() else onStartListening()
        },
        modifier = modifier.scale(scale),
        containerColor = if (isListening) Color(0xFFFF6B6B) else Color(0xFF58A6FF),
        contentColor = Color.White,
        shape = CircleShape
    ) {
        Icon(
            imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
            contentDescription = if (isListening) "Stop listening" else "Start listening",
            modifier = Modifier.size(28.dp)
        )
    }
}