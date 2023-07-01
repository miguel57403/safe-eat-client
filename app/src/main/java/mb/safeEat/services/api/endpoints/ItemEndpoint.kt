package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ItemDto
import mb.safeEat.services.api.models.Item
import retrofit2.http.*

sealed interface ItemEndpoint {
    @GET
    suspend fun findAll(): List<Item>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Item

    @GET("/cart/{cartId}")
    suspend fun findAllByCart(@Path("cartId") cartId: String): List<Item>

    @POST
    suspend fun create( @Body item: ItemDto): Item

    @PUT
    suspend fun update(@Body item: ItemDto): Item

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String): Item
}
