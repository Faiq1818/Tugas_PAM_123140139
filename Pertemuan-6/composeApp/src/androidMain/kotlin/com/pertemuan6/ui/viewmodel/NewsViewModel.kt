package com.pertemuan6.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pertemuan6.data.model.Article
import com.pertemuan6.data.repository.NewsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

sealed class NewsUiState {
    object Loading : NewsUiState()
    data class Success(val articles: List<Article>) : NewsUiState()
    data class Error(val message: String) : NewsUiState()
}

class NewsViewModel(private val repository: NewsRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<NewsUiState>(NewsUiState.Loading)
    val uiState: StateFlow<NewsUiState> = _uiState.asStateFlow()

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()

    private val _selectedArticle = MutableStateFlow<Article?>(null)
    val selectedArticle: StateFlow<Article?> = _selectedArticle.asStateFlow()

    init {
        loadArticles()
    }

    fun loadArticles() {
        viewModelScope.launch {
            _uiState.value = NewsUiState.Loading
            repository.getArticles().fold(
                onSuccess = { _uiState.value = NewsUiState.Success(it) },
                onFailure = { _uiState.value = NewsUiState.Error(it.message ?: "Unknown error") }
            )
        }
    }

    fun refresh() {
        viewModelScope.launch {
            _isRefreshing.value = true
            repository.getArticles().fold(
                onSuccess = { _uiState.value = NewsUiState.Success(it) },
                onFailure = { _uiState.value = NewsUiState.Error(it.message ?: "Unknown error") }
            )
            _isRefreshing.value = false
        }
    }

    fun selectArticle(article: Article) {
        _selectedArticle.value = article
    }
}
