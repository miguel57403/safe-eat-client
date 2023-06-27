package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Cart
import retrofit2.http.*

sealed interface CartEndpoint {
    @GET
    suspend fun findAll(): List<Cart>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Cart

    @GET("/user/{userId}")
    suspend fun findByUser(@Path("userId") userId: String): Cart

    @GET("/{id}/isBuying")
    suspend fun isBuying(@Path("id") id: String): Cart

    @PUT
    suspend fun empty(@Query("cartId") cartId: String?): Cart
}
