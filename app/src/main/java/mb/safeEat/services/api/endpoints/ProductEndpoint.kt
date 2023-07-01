package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ProductDto
import mb.safeEat.services.api.models.Product
import retrofit2.http.*

sealed interface ProductEndpoint {
    @GET
    suspend fun findAll(): List<Product>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Product

    @GET("restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") restaurantId: String): List<Product>

    @GET("restaurant/{restaurantId}/name/{name}")
    suspend fun findAllByRestaurantAndName(
        @Path("restaurantId") restaurantId: String,
        @Path("name") name: String,
    ): List<Product>

    @POST("restaurant/{restaurantId}")
    suspend fun create(
        @Body product: ProductDto,
        @Path("restaurantId") restaurantId: String,
    ): Product

    @PUT
    suspend fun update(@Body product: ProductDto): Product

    // TODO: update the image file type
    @PUT("{id}/image")
    suspend fun updateImage(@Path("id") id: String, @Query("image") imageFile: String): Product

    @DELETE("{id}")
    suspend fun delete(@Path("id") id: String)
}

