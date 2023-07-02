package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestaurantSectionDto
import mb.safeEat.services.api.models.RestaurantSection
import retrofit2.http.*

sealed interface RestaurantSectionEndpoint {
    @GET("/restaurantSections")
    suspend fun findAll(): List<RestaurantSection>

    @GET("/restaurantSections/{id}")
    suspend fun findById(@Path("id") id: String): RestaurantSection

    @POST("/restaurantSections")
    suspend fun create(@Body restaurantSection: RestaurantSectionDto): RestaurantSection

    @PUT("/restaurantSections")
    suspend fun update(@Body restaurantSection: RestaurantSectionDto): RestaurantSection

    @DELETE("/restaurantSections/{id}")
    suspend fun delete(@Path("id") id: String)
}
