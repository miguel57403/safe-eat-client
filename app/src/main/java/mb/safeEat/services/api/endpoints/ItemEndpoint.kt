package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ItemDto
import mb.safeEat.services.api.models.Item
import retrofit2.http.*

sealed interface ItemEndpoint {
    @GET("/items")
    suspend fun findAll(): List<Item>

    @GET("/items/{id}")
    suspend fun findById(@Path("id") id: String): Item

    @GET("/items/cart/{cartId}")
    suspend fun findAllByCart(@Path("cartId") cartId: String): List<Item>

    @POST("/items")
    suspend fun create( @Body item: ItemDto): Item

    @PUT("/items")
    suspend fun update(@Body item: ItemDto): Item

    @DELETE("/items/{id}")
    suspend fun delete(@Path("id") id: String)
}
