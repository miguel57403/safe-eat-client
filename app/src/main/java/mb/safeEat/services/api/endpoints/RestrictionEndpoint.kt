package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Restriction

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

sealed interface RestrictionEndpoint {
    @GET
    fun findAll(): Any

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Any

    @POST
    fun create(@Body restriction: Restriction?): Any

    @POST("/many")
    fun createMany(@Body restrictions: List<Restriction?>?): Any

    @PUT
    fun update(@Body restriction: Restriction?): Any

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Any
}
