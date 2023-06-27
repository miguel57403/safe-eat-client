package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestaurantDto
import mb.safeEat.services.api.models.Restaurant
import retrofit2.http.*

sealed interface RestaurantEndpoint {
    @GET
    suspend fun findAll(): List<Restaurant>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Restaurant

    @GET("/productCategory/{categoryId}")
    suspend fun findAllByCategory(@Path("categoryId") categoryId: String): List<Restaurant>

    @GET("/owner/{userId}")
    suspend fun findAllByOwner(@Path("userId") userId: String): List<Restaurant>

    @GET("/name/{name}")
    suspend fun findAllByName(@Path("name") name: String): List<Restaurant>

    @POST
    suspend fun create(@Body restaurant: RestaurantDto): Restaurant

    @POST("/many")
    suspend fun createMany(@Body restaurants: List<RestaurantDto>): List<Restaurant>

    @PUT
    suspend fun update(@Body restaurant: RestaurantDto): Restaurant

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String)
}
