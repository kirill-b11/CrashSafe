package cs371.finalproject.crashsafe.api.newsapi

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface NewsApi {

    @GET("/v2/everything?q=car%20and%20safety&apiKey=ea59bd7f04f749818e57f86f986716e2")
    suspend fun getArticles() : NewsResponse

    data class NewsResponse(val articles: List<NewsArticle>)

    companion object {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("newsapi.org")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): NewsApi = create(url)
        private fun create(httpUrl: HttpUrl): NewsApi {
            val client = OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().apply {
                    this.level = HttpLoggingInterceptor.Level.BASIC
                })
                .build()
            return Retrofit.Builder()
                .baseUrl(httpUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(NewsApi::class.java)
        }
    }
}