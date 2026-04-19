package com.pertemuan6.data.model

import com.google.gson.annotations.SerializedName

data class Article(
    val id: Int,
    val userId: Int,
    val title: String,
    @SerializedName("body") val description: String,
) {
    val imageUrl: String get() = "https://picsum.photos/seed/$id/600/400"
    val readableTitle: String get() = title.replaceFirstChar { it.uppercase() }
}
