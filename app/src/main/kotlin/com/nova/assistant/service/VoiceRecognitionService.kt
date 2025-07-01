package com.nova.assistant.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class VoiceRecognitionService(private val context: Context) : DefaultLifecycleObserver {
    
    private var speechRecognizer: SpeechRecognizer? = null
    private var isListening = false
    private var onResultListener: ((String) -> Unit)? = null
    
    private val _isRecognizing = MutableStateFlow(false)
    val isRecognizing: StateFlow<Boolean> = _isRecognizing.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    init {
        initializeSpeechRecognizer()
    }
    
    private fun initializeSpeechRecognizer() {
        if (SpeechRecognizer.isRecognitionAvailable(context)) {
            speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context).apply {
                setRecognitionListener(object : RecognitionListener {
                    override fun onReadyForSpeech(params: Bundle?) {
                        _isRecognizing.value = true
                        _error.value = null
                    }
                    
                    override fun onBeginningOfSpeech() {
                        // Speech input has begun
                    }
                    
                    override fun onRmsChanged(rmsdB: Float) {
                        // RMS value of the audio being received
                    }
                    
                    override fun onBufferReceived(buffer: ByteArray?) {
                        // More sound has been received
                    }
                    
                    override fun onEndOfSpeech() {
                        // User has stopped speaking
                    }
                    
                    override fun onError(error: Int) {
                        _isRecognizing.value = false
                        isListening = false
                        
                        val errorMessage = when (error) {
                            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
                            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
                            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
                            SpeechRecognizer.ERROR_NETWORK -> "Network error"
                            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
                            SpeechRecognizer.ERROR_NO_MATCH -> "No speech input matched"
                            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
                            SpeechRecognizer.ERROR_SERVER -> "Server error"
                            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "Speech input timeout"
                            else -> "Unknown recognition error"
                        }
                        _error.value = errorMessage
                    }
                    
                    override fun onResults(results: Bundle?) {
                        _isRecognizing.value = false
                        isListening = false
                        
                        val matches = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            val recognizedText = matches[0]
                            onResultListener?.invoke(recognizedText)
                        }
                    }
                    
                    override fun onPartialResults(partialResults: Bundle?) {
                        val matches = partialResults?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                        if (!matches.isNullOrEmpty()) {
                            // Handle partial results if needed
                        }
                    }
                    
                    override fun onEvent(eventType: Int, params: Bundle?) {
                        // Reserved for future use
                    }
                })
            }
        }
    }
    
    fun startListening() {
        if (!isListening && speechRecognizer != null) {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US")
                putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
                putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 1)
                putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, context.packageName)
                
                // Try to use offline recognition if available
                putExtra(RecognizerIntent.EXTRA_PREFER_OFFLINE, true)
            }
            
            try {
                speechRecognizer?.startListening(intent)
                isListening = true
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to start speech recognition: ${e.message}"
            }
        }
    }
    
    fun stopListening() {
        if (isListening) {
            speechRecognizer?.stopListening()
            isListening = false
            _isRecognizing.value = false
        }
    }
    
    fun setOnResultListener(listener: (String) -> Unit) {
        onResultListener = listener
    }
    
    fun cleanup() {
        stopListening()
        speechRecognizer?.destroy()
        speechRecognizer = null
    }
    
    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        cleanup()
    }
}