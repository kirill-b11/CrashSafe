package cs371.finalproject.crashsafe.api.newsapi

class NewsRepository(private val api: NewsApi) {
    suspend fun fetchQuestions(difficulty: String): NewsApi.NewsResponse {
        return api.getArticles()
    }
}