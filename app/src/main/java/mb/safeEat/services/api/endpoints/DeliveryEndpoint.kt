package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Delivery
import retrofit2.http.*


sealed interface DeliveryEndpoint {

    @GET
    suspend fun findAll(): List<Delivery>

    @GET("/{id}")
    suspend fun findById(@Path("id")id: String): Delivery

    @POST
    fun create(
        @Body delivery: Delivery?,
        @Query("restaurantId") restaurantId: String?): Delivery

    @POST("/many")
    fun createMany(
        @Body deliveries: List<Delivery?>?,
        @Query("restaurantId") restaurantId: String?): List<Delivery>

    @PUT
    fun update(@Body delivery: Delivery?): Delivery

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}