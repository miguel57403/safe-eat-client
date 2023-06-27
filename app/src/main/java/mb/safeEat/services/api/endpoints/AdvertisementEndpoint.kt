package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.AdvertisementDto
import mb.safeEat.services.api.models.Advertisement
import retrofit2.http.*

sealed interface AdvertisementEndpoint {
    @GET
    suspend fun findAll(): List<Advertisement>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Advertisement

    @POST
    suspend fun create(@Body advertisement: AdvertisementDto): Advertisement

    @POST("/many")
    suspend fun createMany(@Body advertisements: List<AdvertisementDto>): List<Advertisement>

    @PUT
    suspend fun update(@Body advertisement: AdvertisementDto): Advertisement

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("restaurantId") restaurantId: String?)
}
