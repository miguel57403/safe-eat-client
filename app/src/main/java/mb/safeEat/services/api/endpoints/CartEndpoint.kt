package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.CartIsEmptyResponseDto
import mb.safeEat.services.api.models.Cart
import retrofit2.http.*

sealed interface CartEndpoint {
    @GET("/carts")
    suspend fun findAll(): List<Cart>

    @GET("/carts/{id}")
    suspend fun findById(@Path("id") id: String): Cart

    @GET("/carts/user/me")
    suspend fun findMe(): Cart

    @GET("/carts/user/{userId}")
    suspend fun findByUser(@Path("userId") userId: String): Cart

    @GET("/carts/isEmpty")
    suspend fun isEmpty(): CartIsEmptyResponseDto

    @PUT("/carts/empty")
    suspend fun empty(): Cart
}
