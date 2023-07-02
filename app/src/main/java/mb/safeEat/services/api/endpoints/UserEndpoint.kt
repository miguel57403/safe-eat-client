package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.UserUpdateDto
import mb.safeEat.services.api.models.User
import retrofit2.http.*

sealed interface UserEndpoint {
    @GET("/users")
    suspend fun findAll(): List<User>

    @GET("/users/{id}")
    suspend fun findById(@Path("id") id: String): User

    @GET("/users/me")
    suspend fun findMe(): User

    @PUT("/users")
    suspend fun update(@Body user: UserUpdateDto): User

    // TODO: update the image file type
    @PUT("/users/me/image")
    suspend fun updateImage(@Query("image") imageFile: String): User

    @DELETE("/users/{id}")
    suspend fun delete(@Path("id") id: String): User
}
