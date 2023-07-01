package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Home
import retrofit2.http.GET

sealed interface HomeEndpoint {
    @GET("/homes")
    suspend fun getOne(): Home
}
