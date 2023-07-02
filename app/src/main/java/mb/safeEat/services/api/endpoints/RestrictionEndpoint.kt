package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestrictionDto
import mb.safeEat.services.api.models.Restriction
import retrofit2.http.*

sealed interface RestrictionEndpoint {
    @GET("/restrictions")
    suspend fun findAll(): List<Restriction>

    @GET("/restrictions/{id}")
    suspend fun findById(@Path("id") id: String): Restriction

    @GET("/restrictions/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Restriction>

    @POST("/restrictions")
    suspend fun create(@Body restriction: RestrictionDto): Restriction

    @PUT("/restrictions")
    suspend fun update(@Body restriction: RestrictionDto): Restriction

    @DELETE("/restrictions/{id}")
    suspend fun delete(@Path("id") id: String): Restriction
}
