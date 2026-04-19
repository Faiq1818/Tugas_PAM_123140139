package com.pertemuan6.data.repository

import com.pertemuan6.data.model.Article
import com.pertemuan6.data.remote.NewsApiService

class NewsRepositoryImpl(
    private val apiService: NewsApiService
) : NewsRepository {

    override suspend fun getArticles(): Result<List<Article>> = runCatching {
        apiService.getArticles()
    }
}
