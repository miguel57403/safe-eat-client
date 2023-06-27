package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.RestrictionDto
import mb.safeEat.services.api.models.Restriction
import retrofit2.http.*

sealed interface RestrictionEndpoint {
    @GET
    suspend fun findAll(): List<Restriction>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Restriction

    @GET("/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Restriction>

    @POST
    suspend fun create(@Body restriction: RestrictionDto): Restriction

    @POST("/many")
    suspend fun createMany(@Body restrictions: List<RestrictionDto>): List<Restriction>

    @PUT
    suspend fun update(@Body restriction: RestrictionDto): Restriction

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String): Restriction
}
