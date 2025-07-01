package com.nova.assistant.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.*

@Composable
fun VoiceScreen(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Title
            Text(
                text = "NOVA Voice Assistant",
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF58A6FF)
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = if (isListening) "Listening..." else "Tap to speak",
                style = MaterialTheme.typography.bodyLarge.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Waveform Animation
            if (isListening) {
                VoiceWaveform(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                )
            } else {
                Spacer(modifier = Modifier.height(100.dp))
            }
            
            Spacer(modifier = Modifier.height(32.dp))
            
            // Main Voice Button
            VoiceButton(
                isListening = isListening,
                onStartListening = onStartListening,
                onStopListening = onStopListening
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Instructions
            Text(
                text = if (isListening) {
                    "Say a command like:\n\"Turn on WiFi\" or \"Call Mom\""
                } else {
                    "Try saying \"Hey Nova\" or tap the mic button"
                },
                style = MaterialTheme.typography.bodyMedium.copy(
                    color = Color.Gray
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
fun VoiceButton(
    isListening: Boolean,
    onStartListening: () -> Unit,
    onStopListening: () -> Unit
) {
    val scale by animateFloatAsState(
        targetValue = if (isListening) 1.1f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "buttonScale"
    )
    
    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = if (isListening) 1.2f else 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulseScale"
    )
    
    Box(
        contentAlignment = Alignment.Center
    ) {
        // Outer glow effect when listening
        if (isListening) {
            Box(
                modifier = Modifier
                    .size((120 * pulseScale).dp)
                    .clip(CircleShape)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF58A6FF).copy(alpha = 0.3f),
                                Color.Transparent
                            )
                        )
                    )
            )
        }
        
        // Main button
        FloatingActionButton(
            onClick = {
                if (isListening) onStopListening() else onStartListening()
            },
            modifier = Modifier
                .size(80.dp)
                .scale(scale),
            containerColor = if (isListening) Color(0xFFFF6B6B) else Color(0xFF58A6FF),
            contentColor = Color.White,
            shape = CircleShape
        ) {
            Icon(
                imageVector = if (isListening) Icons.Default.Stop else Icons.Default.Mic,
                contentDescription = if (isListening) "Stop listening" else "Start listening",
                modifier = Modifier.size(32.dp)
            )
        }
    }
}

@Composable
fun VoiceWaveform(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "waveform")
    
    val animationValues = remember {
        (0..20).map { index ->
            infiniteTransition.animateFloat(
                initialValue = 0.1f,
                targetValue = 1f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000 + (index * 100),
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Reverse
                ),
                label = "wave$index"
            )
        }
    }
    
    Canvas(modifier = modifier) {
        drawWaveform(animationValues.map { it.value })
    }
}

private fun DrawScope.drawWaveform(amplitudes: List<Float>) {
    val width = size.width
    val height = size.height
    val centerY = height / 2f
    val barWidth = width / amplitudes.size
    val maxBarHeight = height * 0.8f
    
    amplitudes.forEachIndexed { index, amplitude ->
        val barHeight = amplitude * maxBarHeight
        val startX = index * barWidth + barWidth * 0.2f
        val endX = startX + barWidth * 0.6f
        
        val gradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF58A6FF),
                Color(0xFF7C3AED),
                Color(0xFF58A6FF)
            ),
            startY = centerY - barHeight / 2f,
            endY = centerY + barHeight / 2f
        )
        
        drawLine(
            brush = gradient,
            start = Offset(startX + (endX - startX) / 2f, centerY - barHeight / 2f),
            end = Offset(startX + (endX - startX) / 2f, centerY + barHeight / 2f),
            strokeWidth = barWidth * 0.6f
        )
    }
}