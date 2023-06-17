package mb.safeEat.services.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

sealed interface AuthEndpoint {
    @POST("login")
    suspend fun login(@Body body: LoginBody): LoginResponse

    @GET("me")
    suspend fun me(): UserResponse
}

data class LoginBody(
    val username: String,
    val password: String
)

data class LoginResponse(
    val accessToken: String,
    val tokenType: String
)

data class UserResponse(
    val id: String,
    val username: String,
    val email: String
)
