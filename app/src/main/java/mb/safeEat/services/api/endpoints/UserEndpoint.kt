package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.UserUpdateDto
import mb.safeEat.services.api.models.User
import okhttp3.MultipartBody
import retrofit2.http.*

sealed interface UserEndpoint {
    @GET("/users")
    suspend fun findAll(): List<User>

    @GET("/users/{id}")
    suspend fun findById(@Path("id") id: String): User

    @GET("/users/me")
    suspend fun findMe(): User

    @PUT("/users/me")
    suspend fun update(@Body user: UserUpdateDto): User

    // TODO: update the image file type
    @Multipart
    @PUT("/users/me/image")
    suspend fun updateImage(@Part("image") image: MultipartBody.Part): User

    @DELETE("/users/{id}")
    suspend fun delete(@Path("id") id: String): User
}
