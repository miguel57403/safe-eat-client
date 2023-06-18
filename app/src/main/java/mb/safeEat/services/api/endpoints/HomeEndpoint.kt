package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Home
import retrofit2.http.*

sealed interface HomeEndpoint {
    @GET
    suspend fun findAll(): List<Home>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String?): Home

    @POST
    suspend fun create(): Home

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String?)
}