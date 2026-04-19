package com.pertemuan6.data.repository

import com.pertemuan6.data.model.Article

interface NewsRepository {
    suspend fun getArticles(): Result<List<Article>>
}
