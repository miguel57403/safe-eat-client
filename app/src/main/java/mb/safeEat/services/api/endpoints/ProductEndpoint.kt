package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ProductDto
import mb.safeEat.services.api.models.Product
import retrofit2.http.*

sealed interface ProductEndpoint {
    @GET
    suspend fun findAll(): List<Product>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Product

    @GET("/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") restaurantId: String): List<Product>

    @GET("/restaurant/{restaurantId}/name/{name}")
    suspend fun findAllByRestaurantAndName(
        @Path("restaurantId") restaurantId: String,
        @Path("name") name: String,
    ): List<Product>

    @POST
    suspend fun create(
        @Body product: ProductDto,
        @Query("restaurantId") restaurantId: String,
    ): Product

    @POST("/many")
    suspend fun createMany(
        @Body products: List<ProductDto>,
        @Query("restaurantId") restaurantId: String,
    ): List<Product>

    @PUT
    suspend fun update(@Body product: ProductDto): Product

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("restaurantId") restaurantId: String)
}

