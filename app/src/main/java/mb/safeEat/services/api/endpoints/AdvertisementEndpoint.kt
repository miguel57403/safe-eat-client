package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.AdvertisementDto
import mb.safeEat.services.api.models.Advertisement
import retrofit2.http.*

sealed interface AdvertisementEndpoint {
    @GET("/advertisements")
    suspend fun findAll(): List<Advertisement>

    @GET("/advertisements/{id}")
    suspend fun findById(@Path("id") id: String): Advertisement

    @GET("/advertisements/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") id: String): List<Advertisement>

    @POST("/advertisements")
    suspend fun create(@Body advertisement: AdvertisementDto): Advertisement

    @PUT("/advertisements")
    suspend fun update(@Body advertisement: AdvertisementDto): Advertisement

    // TODO: update the image file type
    @PUT("/advertisements/{id}/image")
    suspend fun updateImage(
        @Path("id") id: String,
        @Query("image") imageFile: String
    ): Advertisement

    @DELETE("/advertisements/{id}")
    suspend fun delete(@Path("id") id: String)
}
