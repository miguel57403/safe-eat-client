package mb.safeEat.services.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class Api {
    val auth: AuthEndpoint = jsonApi("${config.baseUrl}/auth/").create(AuthEndpoint::class.java)

    companion object {
        fun jsonApi(baseUrl: String): Retrofit {
            val okHttpClient = OkHttpClient.Builder()
                .readTimeout(1, TimeUnit.SECONDS)
                .connectTimeout(1, TimeUnit.SECONDS)
                .addInterceptor(authorization)
                .build()
            return Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }
}

val api = Api()
