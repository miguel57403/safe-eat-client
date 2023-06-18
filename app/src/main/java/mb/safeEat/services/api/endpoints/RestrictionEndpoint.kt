package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Restriction
import retrofit2.http.*

sealed interface RestrictionEndpoint {
    @GET
    fun findAll(): List<Restriction>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Restriction

    @POST
    fun create(@Body restriction: Restriction?): Restriction

    @POST("/many")
    fun createMany(@Body restrictions: List<Restriction?>?): List<Restriction>

    @PUT
    fun update(@Body restriction: Restriction?): Restriction

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Restriction
}
