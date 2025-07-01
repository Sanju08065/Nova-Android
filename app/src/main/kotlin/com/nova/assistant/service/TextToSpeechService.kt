package com.nova.assistant.service

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.*

class TextToSpeechService(private val context: Context) {
    
    private var textToSpeech: TextToSpeech? = null
    private var isInitialized = false
    
    private val _isSpeaking = MutableStateFlow(false)
    val isSpeaking: StateFlow<Boolean> = _isSpeaking.asStateFlow()
    
    private val _initializationError = MutableStateFlow<String?>(null)
    val initializationError: StateFlow<String?> = _initializationError.asStateFlow()
    
    fun initialize() {
        textToSpeech = TextToSpeech(context) { status ->
            when (status) {
                TextToSpeech.SUCCESS -> {
                    isInitialized = true
                    setupTTS()
                    _initializationError.value = null
                }
                TextToSpeech.ERROR -> {
                    isInitialized = false
                    _initializationError.value = "Failed to initialize Text-to-Speech engine"
                }
            }
        }
    }
    
    private fun setupTTS() {
        textToSpeech?.let { tts ->
            // Set language to US English
            val result = tts.setLanguage(Locale.US)
            
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                _initializationError.value = "Language not supported"
                return
            }
            
            // Set speech rate and pitch
            tts.setSpeechRate(1.0f) // Normal speed
            tts.setPitch(1.0f) // Normal pitch
            
            // Set up utterance progress listener
            tts.setOnUtteranceProgressListener(object : UtteranceProgressListener() {
                override fun onStart(utteranceId: String?) {
                    _isSpeaking.value = true
                }
                
                override fun onDone(utteranceId: String?) {
                    _isSpeaking.value = false
                }
                
                override fun onError(utteranceId: String?) {
                    _isSpeaking.value = false
                }
            })
        }
    }
    
    fun speak(text: String, queueMode: Int = TextToSpeech.QUEUE_FLUSH) {
        if (!isInitialized || textToSpeech == null) {
            _initializationError.value = "TTS not initialized"
            return
        }
        
        if (text.isBlank()) return
        
        try {
            val utteranceId = "nova_${System.currentTimeMillis()}"
            val params = Bundle().apply {
                putString(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, utteranceId)
            }
            
            textToSpeech?.speak(text, queueMode, params, utteranceId)
        } catch (e: Exception) {
            _initializationError.value = "TTS error: ${e.message}"
        }
    }
    
    fun stop() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }
    
    fun pause() {
        textToSpeech?.stop()
        _isSpeaking.value = false
    }
    
    fun setSpeechRate(rate: Float) {
        textToSpeech?.setSpeechRate(rate.coerceIn(0.1f, 3.0f))
    }
    
    fun setPitch(pitch: Float) {
        textToSpeech?.setPitch(pitch.coerceIn(0.1f, 2.0f))
    }
    
    fun setLanguage(locale: Locale): Boolean {
        return textToSpeech?.let { tts ->
            val result = tts.setLanguage(locale)
            result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED
        } ?: false
    }
    
    fun getAvailableLanguages(): Set<Locale>? {
        return textToSpeech?.availableLanguages
    }
    
    fun isLanguageAvailable(locale: Locale): Boolean {
        return textToSpeech?.let { tts ->
            val result = tts.isLanguageAvailable(locale)
            result == TextToSpeech.LANG_AVAILABLE || result == TextToSpeech.LANG_COUNTRY_AVAILABLE
        } ?: false
    }
    
    fun speakWithPriority(text: String, priority: SpeechPriority = SpeechPriority.NORMAL) {
        val queueMode = when (priority) {
            SpeechPriority.HIGH -> TextToSpeech.QUEUE_FLUSH
            SpeechPriority.NORMAL -> TextToSpeech.QUEUE_ADD
            SpeechPriority.LOW -> TextToSpeech.QUEUE_ADD
        }
        speak(text, queueMode)
    }
    
    fun speakError(errorMessage: String) {
        speakWithPriority("Error: $errorMessage", SpeechPriority.HIGH)
    }
    
    fun speakSuccess(successMessage: String) {
        speakWithPriority(successMessage, SpeechPriority.NORMAL)
    }
    
    fun speakCommandResult(commandName: String, result: String) {
        val message = "$commandName completed. $result"
        speakWithPriority(message, SpeechPriority.NORMAL)
    }
    
    fun shutdown() {
        stop()
        textToSpeech?.shutdown()
        textToSpeech = null
        isInitialized = false
        _isSpeaking.value = false
    }
}

enum class SpeechPriority {
    HIGH,   // Interrupts current speech
    NORMAL, // Queues after current speech
    LOW     // Queues at end
}