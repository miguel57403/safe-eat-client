package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.DeliveryDto
import mb.safeEat.services.api.models.Delivery
import retrofit2.http.*

sealed interface DeliveryEndpoint {
    @GET
    suspend fun findAll(): List<Delivery>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Delivery

    @POST
    suspend fun create(
        @Body delivery: DeliveryDto,
        @Query("restaurantId") restaurantId: String,
    ): Delivery

    @POST("/many")
    suspend fun createMany(
        @Body deliveries: List<DeliveryDto>,
        @Query("restaurantId") restaurantId: String,
    ): List<Delivery>

    @PUT
    suspend fun update(@Body delivery: DeliveryDto): Delivery

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("restaurantId") restaurantId: String?)
}
