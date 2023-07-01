package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.AdvertisementDto
import mb.safeEat.services.api.models.Advertisement
import retrofit2.http.*

sealed interface AdvertisementEndpoint {
    @GET
    suspend fun findAll(): List<Advertisement>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Advertisement

    @GET("/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") id: String): List<Advertisement>

    @POST
    suspend fun create(@Body advertisement: AdvertisementDto): Advertisement

    @PUT
    suspend fun update(@Body advertisement: AdvertisementDto): Advertisement

    // TODO: update the image file type
    @PUT("/{id}/image")
    suspend fun updateImage(
        @Path("id") id: String,
        @Query("image") imageFile: String
    ): Advertisement

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String)
}
