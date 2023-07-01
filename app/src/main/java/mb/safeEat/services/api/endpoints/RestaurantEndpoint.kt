package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestaurantDto
import mb.safeEat.services.api.models.Restaurant
import retrofit2.http.*

sealed interface RestaurantEndpoint {
    @GET("/restaurants")
    suspend fun findAll(): List<Restaurant>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Restaurant

    @GET("productCategory/{categoryId}")
    suspend fun findAllByCategory(@Path("categoryId") categoryId: String): List<Restaurant>

    @GET("owner/{userId}")
    suspend fun findAllByOwner(@Path("userId") userId: String): List<Restaurant>

    @GET("cart")
    suspend fun findByCart(): Restaurant

    @GET("name/{name}")
    suspend fun findAllByName(@Path("name") name: String): List<Restaurant>

    @POST
    suspend fun create(@Body restaurant: RestaurantDto): Restaurant

    @PUT
    suspend fun update(@Body restaurant: RestaurantDto): Restaurant

    // TODO: update the image file type
    @PUT("{id}/logo")
    suspend fun updateLogo(@Path("id") id: String, @Query("image") imageFile: String): Restaurant

    // TODO: update the image file type
    @PUT("{id}/cover")
    suspend fun updateCover(@Path("id") id: String, @Query("image") imageFile: String): Restaurant

    @DELETE("{id}")
    suspend fun delete(@Path("id") id: String)
}
