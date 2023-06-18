package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Advertisement
import retrofit2.http.*

sealed interface AdvertisementEndpoint {
    @GET
    suspend fun findAll(): List<Advertisement>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Advertisement

    @POST
    fun create(@Body advertisement: Advertisement?): Advertisement

    @POST("/many")
    fun createMany(@Body advertisements: List<Advertisement?>?): List<Advertisement>

    @PUT
    fun update(@Body advertisement: Advertisement?): Advertisement

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}
