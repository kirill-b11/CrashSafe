package cs371.finalproject.crashsafe.api.newsapi

class NewsRepository(private val api: NewsApi) {
    suspend fun fetchArticles(): List<NewsArticle> {
        return api.getArticles().results
    }
}