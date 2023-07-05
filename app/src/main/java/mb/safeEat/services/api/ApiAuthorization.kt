package mb.safeEat.services.api

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class ApiAuthorization : Interceptor {
    private var authorization: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        interceptAndLog(chain)
        return interceptAndAuthorize(chain)
    }

    fun setAuthorization(authorization: String?) {
        this.authorization = authorization
    }

    fun clearAuthorization() {
        this.authorization = null
    }

    private fun interceptAndLog(chain: Interceptor.Chain) {
        when (config.logLevel) {
            ApiLogLevel.None -> return
            ApiLogLevel.Basic -> Log.d("Api", "${chain.request().method()} ${chain.request().url()}")
            ApiLogLevel.Full -> Log.d("Api", "${chain.request().method()} ${chain.request().url()} authorization=[$authorization]")
        }
    }

    private fun interceptAndAuthorize(chain: Interceptor.Chain): Response {
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
}

val authorization = ApiAuthorization()
