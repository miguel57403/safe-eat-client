package mb.safeEat.services.api.endpoints

import retrofit2.http.GET

sealed interface HelloEndpoint {
    @GET("")
    suspend fun hello(): String
}
