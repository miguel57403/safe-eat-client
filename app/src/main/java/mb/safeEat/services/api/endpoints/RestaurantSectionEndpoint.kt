package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestaurantSectionDto
import mb.safeEat.services.api.models.RestaurantSection
import retrofit2.http.*

sealed interface RestaurantSectionEndpoint {
    @GET
    suspend fun findAll(): List<RestaurantSection>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): RestaurantSection

    @POST
    suspend fun create(@Body restaurantSection: RestaurantSectionDto): RestaurantSection

    @POST("/many")
    suspend fun createMany(@Body restaurantSections: List<RestaurantSectionDto>): List<RestaurantSection>

    @PUT
    suspend fun update(@Body restaurantSection: RestaurantSectionDto): RestaurantSection

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String)
}
