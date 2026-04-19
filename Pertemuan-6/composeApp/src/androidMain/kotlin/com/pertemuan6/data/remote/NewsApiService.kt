package com.pertemuan6.data.remote

import com.pertemuan6.data.model.Article
import retrofit2.http.GET

interface NewsApiService {
    @GET("posts")
    suspend fun getArticles(): List<Article>
}
