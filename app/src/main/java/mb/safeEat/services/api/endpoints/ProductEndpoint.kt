package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ProductDto
import mb.safeEat.services.api.models.Product
import retrofit2.http.*

sealed interface ProductEndpoint {
    @GET("/products")
    suspend fun findAll(): List<Product>

    @GET("/products/{id}")
    suspend fun findById(@Path("id") id: String): Product

    @GET("/products/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") restaurantId: String): List<Product>

    @GET("/products/restaurant/{restaurantId}/name/{name}")
    suspend fun findAllByRestaurantAndName(
        @Path("restaurantId") restaurantId: String,
        @Path("name") name: String,
    ): List<Product>

    @POST("/products/restaurant/{restaurantId}")
    suspend fun create(
        @Body product: ProductDto,
        @Path("restaurantId") restaurantId: String,
    ): Product

    @PUT("/products")
    suspend fun update(@Body product: ProductDto): Product

    // TODO: update the image file type
    @PUT("/products/{id}/image")
    suspend fun updateImage(@Path("id") id: String, @Query("image") imageFile: String): Product

    @DELETE("/products/{id}")
    suspend fun delete(@Path("id") id: String)
}

