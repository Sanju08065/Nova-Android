package com.nova.assistant.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.nova.assistant.data.model.Command
import com.nova.assistant.data.model.CommandCategory
import com.nova.assistant.data.repository.CommandRepository
import com.nova.assistant.service.VoiceRecognitionService
import com.nova.assistant.service.TextToSpeechService
import com.nova.assistant.service.CommandExecutorService

class NovaViewModel(application: Application) : AndroidViewModel(application) {
    
    private val commandRepository = CommandRepository()
    private val voiceRecognitionService = VoiceRecognitionService(application)
    private val textToSpeechService = TextToSpeechService(application)
    private val commandExecutorService = CommandExecutorService(application)
    
    private val _uiState = MutableStateFlow(NovaUiState())
    val uiState: StateFlow<NovaUiState> = _uiState.asStateFlow()
    
    private val _isListening = MutableStateFlow(false)
    val isListening: StateFlow<Boolean> = _isListening.asStateFlow()
    
    private val _recognizedText = MutableStateFlow("")
    val recognizedText: StateFlow<String> = _recognizedText.asStateFlow()
    
    private val _currentCategory = MutableStateFlow(CommandCategory.ALL)
    val currentCategory: StateFlow<CommandCategory> = _currentCategory.asStateFlow()
    
    private val _commands = MutableStateFlow<List<Command>>(emptyList())
    val commands: StateFlow<List<Command>> = _commands.asStateFlow()
    
    init {
        loadCommands()
        initializeServices()
    }
    
    private fun loadCommands() {
        viewModelScope.launch {
            _commands.value = commandRepository.getAllCommands()
        }
    }
    
    private fun initializeServices() {
        textToSpeechService.initialize()
        voiceRecognitionService.setOnResultListener { result ->
            _recognizedText.value = result
            processVoiceCommand(result)
        }
    }
    
    fun startListening() {
        if (!_isListening.value) {
            _isListening.value = true
            voiceRecognitionService.startListening()
            _uiState.value = _uiState.value.copy(
                isListening = true,
                statusMessage = "Listening..."
            )
        }
    }
    
    fun stopListening() {
        if (_isListening.value) {
            _isListening.value = false
            voiceRecognitionService.stopListening()
            _uiState.value = _uiState.value.copy(
                isListening = false,
                statusMessage = "Ready"
            )
        }
    }
    
    fun executeCommand(command: Command) {
        viewModelScope.launch {
            try {
                _uiState.value = _uiState.value.copy(
                    isExecuting = true,
                    statusMessage = "Executing: ${command.name}"
                )
                
                val result = commandExecutorService.executeCommand(command)
                
                if (result.success) {
                    _uiState.value = _uiState.value.copy(
                        isExecuting = false,
                        statusMessage = result.message,
                        lastExecutedCommand = command
                    )
                    textToSpeechService.speak(result.message)
                } else {
                    _uiState.value = _uiState.value.copy(
                        isExecuting = false,
                        statusMessage = "Error: ${result.message}"
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isExecuting = false,
                    statusMessage = "Error executing command: ${e.message}"
                )
            }
        }
    }
    
    private fun processVoiceCommand(spokenText: String) {
        viewModelScope.launch {
            val matchedCommand = commandRepository.findCommandByVoiceInput(spokenText)
            if (matchedCommand != null) {
                executeCommand(matchedCommand)
            } else {
                _uiState.value = _uiState.value.copy(
                    statusMessage = "Command not recognized: $spokenText"
                )
                textToSpeechService.speak("Sorry, I didn't understand that command.")
            }
        }
    }
    
    fun filterCommandsByCategory(category: CommandCategory) {
        _currentCategory.value = category
        viewModelScope.launch {
            val filteredCommands = if (category == CommandCategory.ALL) {
                commandRepository.getAllCommands()
            } else {
                commandRepository.getCommandsByCategory(category)
            }
            _commands.value = filteredCommands
        }
    }
    
    fun searchCommands(query: String) {
        viewModelScope.launch {
            val searchResults = commandRepository.searchCommands(query)
            _commands.value = searchResults
        }
    }
    
    fun toggleWakeWordDetection() {
        val newState = !_uiState.value.wakeWordEnabled
        _uiState.value = _uiState.value.copy(wakeWordEnabled = newState)
        // TODO: Implement wake word service toggle
    }
    
    override fun onCleared() {
        super.onCleared()
        textToSpeechService.shutdown()
        voiceRecognitionService.cleanup()
    }
}

data class NovaUiState(
    val isListening: Boolean = false,
    val isExecuting: Boolean = false,
    val statusMessage: String = "Ready",
    val wakeWordEnabled: Boolean = false,
    val lastExecutedCommand: Command? = null,
    val currentTab: Int = 0
)