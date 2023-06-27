package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.UserDto
import mb.safeEat.services.api.models.User
import retrofit2.http.*

sealed interface UserEndpoint {
    @GET
    suspend fun findAll(): List<User>

    @GET("/me")
    suspend fun getMe(): User

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): User

    @POST("/many")
    suspend fun createMany(@Body users: List<UserDto>): List<User>

    @PUT
    suspend fun update(@Body user: UserDto): User

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String): User
}
