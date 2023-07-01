package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.DeliveryDto
import mb.safeEat.services.api.models.Delivery
import retrofit2.http.*

sealed interface DeliveryEndpoint {
    @GET
    suspend fun findAll(): List<Delivery>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Delivery

    @GET("restaurant/{restaurantId}")
    suspend fun findByAllRestaurant(@Path("restaurantId") restaurantId: String): List<Delivery>

    @POST("restaurant/{restaurantId}")
    suspend fun create(
        @Body delivery: DeliveryDto,
        @Path("restaurantId") restaurantId: String,
    ): Delivery

    @PUT
    suspend fun update(@Body delivery: DeliveryDto): Delivery

    @DELETE("{id}")
    suspend fun delete(@Path("id") id: String)
}
