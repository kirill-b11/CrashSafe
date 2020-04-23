package cs371.finalproject.crashsafe.api.newsapi

import android.util.Log

class NewsRepository(private val api: NewsApi) {
    suspend fun fetchArticles(): List<NewsArticle> {
        return api.getArticles().articles
    }
}