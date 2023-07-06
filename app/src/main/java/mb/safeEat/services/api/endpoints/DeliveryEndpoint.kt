package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.DeliveryDto
import mb.safeEat.services.api.models.Delivery
import retrofit2.http.*

sealed interface DeliveryEndpoint {
    @GET("/deliveries")
    suspend fun findAll(): List<Delivery>

    @GET("/deliveries/{id}")
    suspend fun findById(@Path("id") id: String): Delivery

    @GET("/deliveries/restaurant/{restaurantId}")
    suspend fun findByAllRestaurant(@Path("restaurantId") restaurantId: String): List<Delivery>

    @POST("/deliveries/restaurant/{restaurantId}")
    suspend fun create(
        @Body delivery: DeliveryDto,
        @Path("restaurantId") restaurantId: String,
    ): Delivery

    @PUT("/deliveries")
    suspend fun update(@Body delivery: DeliveryDto): Delivery

    @PUT("/deliveries/select/{id}")
    suspend fun select(@Path("id") id: String): Delivery

    @DELETE("/deliveries/{id}")
    suspend fun delete(@Path("id") id: String)
}
