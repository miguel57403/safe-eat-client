package mb.safeEat.services.api

import okhttp3.Interceptor
import okhttp3.Response

class ApiAuthorization : Interceptor {
    private var authorization: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        return if (authorization == null) {
            chain.proceed(chain.request())
        } else {
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
