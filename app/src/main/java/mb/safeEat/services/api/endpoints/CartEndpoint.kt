package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Cart
import retrofit2.http.*

sealed interface CartEndpoint {
    @GET
    suspend fun findAll(): List<Cart>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Cart

    @GET("user/{userId}")
    suspend fun findByUser(@Path("userId") userId: String): Cart

    @GET("isEmpty")
    suspend fun isEmpty(): Boolean

    @PUT("empty")
    suspend fun empty(): Cart
}
