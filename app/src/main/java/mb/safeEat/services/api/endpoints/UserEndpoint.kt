package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.UserDto
import mb.safeEat.services.api.models.User
import retrofit2.http.*

sealed interface UserEndpoint {
    @GET
    suspend fun findAll(): List<User>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): User

    @GET("/me")
    suspend fun findMe(): User

    @PUT
    suspend fun update(@Body user: UserDto): User

    // TODO: update the image file type
    @PUT("/me/image")
    suspend fun updateImage(@Query("image") imageFile: String): User


    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String): User
}
