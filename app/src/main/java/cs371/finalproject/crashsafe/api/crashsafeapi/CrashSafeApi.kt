package cs371.finalproject.crashsafe.api.crashsafeapi

import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface CrashSafeApi {

    @GET("/models?")
    suspend fun searchModels(@Query("query") searchStr: String) : List<VehicleModel>

    @GET("/model?")
    suspend fun getModel(@Query("name") modelName: String) : VehicleModel

    companion object {
        var url = HttpUrl.Builder()
            .scheme("https")
            .host("api.crashsafe.me")
            .build()

        // Public create function that ties together building the base
        // URL and the private create function that initializes Retrofit
        fun create(): CrashSafeApi = create(url)
        private fun create(httpUrl: HttpUrl): CrashSafeApi {
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
                .create(CrashSafeApi::class.java)
        }
    }
}