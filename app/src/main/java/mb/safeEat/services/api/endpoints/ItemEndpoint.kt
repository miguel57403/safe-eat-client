package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Item
import retrofit2.http.*

sealed interface ItemEndpoint {
    @GET
    fun findAll(): List<Item>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Item

    @POST
    fun create(
        @Body item: Item?,
        @Query("cartId") cartId: String?,
    ): Item

    @POST("/many")
    fun createMany(
        @Body items: List<Item?>?,
        @Query("cartId") cartId: String?,
    ): List<Item>

    @PUT
    fun update(@Body item: Item?): Item

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Item
}
