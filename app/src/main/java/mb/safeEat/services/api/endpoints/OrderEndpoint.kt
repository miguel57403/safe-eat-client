package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Order
import retrofit2.http.*


sealed interface OrderEndpoint{

    @GET
    fun findAll(): List<Order>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Order

    @POST
    fun create(@Body order: Order?): Order

    @POST("/many")
    fun createMany(@Body orders: List<Order?>?): List<Order>

    @PUT
    fun update(@Body order: Order?): Order

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Order
}