package mb.safeEat.services.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ApiAuthorization : Interceptor {
    private var authorization: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (authorization == null) {
            Log.d("ApiAuthorization", "No authorization ${chain.request().url()}")
            chain.proceed(chain.request())
        } else {
            Log.d("ApiAuthorization", "Authorization ${chain.request().url()}: $authorization")
            chain.proceed(
                chain.request().newBuilder()
                    .header("Authorization", authorization!!)
                    .build()
            )
        }
    }

    fun setAuthorization(authorization: String?) {
        this.authorization = authorization
    }

    fun clearAuthorization() {
        this.authorization = null
    }
}

val authorization = ApiAuthorization()
