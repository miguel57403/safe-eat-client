package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Login
import retrofit2.http.POST

sealed interface LoginEndpoint {
    @POST("")
    suspend fun login(): Login
}
