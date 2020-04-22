package cs371.finalproject.crashsafe.api.newsapi

import com.google.gson.annotations.SerializedName

data class NewsArticle (
    @SerializedName("title")
    val title: String,
    @SerializedName("description")
    val description: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("urlToImage")
    val imageUrl: String,
    @SerializedName("content")
    val content: String
)