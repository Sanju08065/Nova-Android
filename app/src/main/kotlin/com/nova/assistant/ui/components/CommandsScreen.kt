package com.nova.assistant.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.nova.assistant.data.model.Command
import com.nova.assistant.data.model.CommandCategory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CommandsScreen(
    commands: List<Command>,
    currentCategory: CommandCategory,
    onCategorySelected: (CommandCategory) -> Unit,
    onCommandExecute: (Command) -> Unit,
    onSearchCommands: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { 
                searchQuery = it
                onSearchCommands(it)
            },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search commands...") },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = Color(0xFF58A6FF)
                )
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = Color(0xFF58A6FF),
                unfocusedBorderColor = Color.Gray,
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White
            ),
            shape = RoundedCornerShape(12.dp)
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Category Filter
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(CommandCategory.values()) { category ->
                CategoryChip(
                    category = category,
                    isSelected = category == currentCategory,
                    onClick = { onCategorySelected(category) }
                )
            }
        }
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Commands List
        Text(
            text = "${commands.size} Commands",
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(commands) { command ->
                CommandCard(
                    command = command,
                    onExecute = { onCommandExecute(command) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    category: CommandCategory,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    FilterChip(
        onClick = onClick,
        label = { 
            Text(
                text = category.displayName,
                color = if (isSelected) Color.White else Color.Gray
            ) 
        },
        selected = isSelected,
        colors = FilterChipDefaults.filterChipColors(
            selectedContainerColor = Color(0xFF58A6FF),
            containerColor = Color(0xFF21262D),
            selectedLabelColor = Color.White,
            labelColor = Color.Gray
        ),
        border = FilterChipDefaults.filterChipBorder(
            borderColor = if (isSelected) Color(0xFF58A6FF) else Color.Gray,
            selectedBorderColor = Color(0xFF58A6FF)
        )
    )
}

@Composable
fun CommandCard(
    command: Command,
    onExecute: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onExecute() },
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF21262D).copy(alpha = 0.8f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Command Icon
            Icon(
                painter = painterResource(id = command.iconRes),
                contentDescription = command.name,
                tint = Color(0xFF58A6FF),
                modifier = Modifier.size(24.dp)
            )
            
            Spacer(modifier = Modifier.width(16.dp))
            
            // Command Details
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = command.name,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                )
                
                Text(
                    text = command.description,
                    style = MaterialTheme.typography.bodySmall.copy(
                        color = Color.Gray
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                if (command.voiceTriggers.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Say: \"${command.voiceTriggers.first()}\"",
                        style = MaterialTheme.typography.bodySmall.copy(
                            color = Color(0xFF7C3AED),
                            fontWeight = FontWeight.Medium
                        ),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(8.dp))
            
            // Execute Button
            IconButton(
                onClick = onExecute,
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF58A6FF).copy(alpha = 0.2f))
            ) {
                Icon(
                    imageVector = Icons.Default.PlayArrow,
                    contentDescription = "Execute command",
                    tint = Color(0xFF58A6FF),
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}