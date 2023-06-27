package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Home
import retrofit2.http.*

sealed interface HomeEndpoint {
    @GET
    suspend fun get(): Home
}
