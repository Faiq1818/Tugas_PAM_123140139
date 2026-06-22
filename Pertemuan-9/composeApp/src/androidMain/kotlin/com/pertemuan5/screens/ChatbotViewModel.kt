package com.pertemuan5.screens

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pertemuan5.data.ChatbotRepository
import com.pertemuan5.data.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class ChatMessage(
    val id: String = java.util.UUID.randomUUID().toString(),
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class ChatbotViewModel(
    private val chatbotRepository: ChatbotRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    val apiKey: StateFlow<String> = settingsRepository.aiApiKey
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = ""
        )

    val apiModel: StateFlow<String> = settingsRepository.aiModel
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = "gemini-1.5-flash"
        )

    private val _messages = MutableStateFlow<List<ChatMessage>>(
        listOf(
            ChatMessage(
                text = "Halo! Saya adalah Smart Assistant Anda. Silakan tanyakan apa saja. Pastikan API Key Anda sudah dikonfigurasi.",
                isUser = false
            )
        )
    )
    val messages: StateFlow<List<ChatMessage>> = _messages.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun updateApiKey(key: String) {
        viewModelScope.launch {
            settingsRepository.setAiApiKey(key)
        }
    }

    fun updateApiModel(model: String) {
        viewModelScope.launch {
            settingsRepository.setAiModel(model)
        }
    }

    fun sendMessage(prompt: String) {
        if (prompt.isBlank()) return

        val userMessage = ChatMessage(text = prompt.trim(), isUser = true)
        _messages.value = _messages.value + userMessage

        _isLoading.value = true
        viewModelScope.launch {
            val responseText = chatbotRepository.generateResponse(
                prompt = prompt,
                apiKey = apiKey.value,
                model = apiModel.value
            )
            val botMessage = ChatMessage(text = responseText, isUser = false)
            _messages.value = _messages.value + botMessage
            _isLoading.value = false
        }
    }

    fun clearChat() {
        _messages.value = listOf(
            ChatMessage(
                text = "Halo! Saya adalah Smart Assistant Anda. Silakan tanyakan apa saja. Pastikan API Key Anda sudah dikonfigurasi.",
                isUser = false
            )
        )
    }
}
