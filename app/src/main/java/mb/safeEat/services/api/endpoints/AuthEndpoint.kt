package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.LoginDto
import mb.safeEat.services.api.dto.LoginResponseDto
import mb.safeEat.services.api.dto.UserDto
import mb.safeEat.services.api.models.User
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

sealed interface AuthEndpoint {
    @GET("/signup")
    suspend fun signup(@Body body: UserDto): User

    @POST("/login")
    suspend fun login(@Body body: LoginDto): LoginResponseDto
}
