package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.OrderDto
import mb.safeEat.services.api.models.Order
import retrofit2.http.*

sealed interface OrderEndpoint {
    @GET
    suspend fun findAll(): List<Order>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Order

    @GET("/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Order>

    @POST
    suspend fun create(@Body order: OrderDto): Order

    @POST("/many")
    suspend fun createMany(@Body orders: List<OrderDto>): List<Order>

    @PUT("/{id}")
    suspend fun updateStatus(@Path("id") id: String, @Query("status") status: String): Order

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String): Order
}
