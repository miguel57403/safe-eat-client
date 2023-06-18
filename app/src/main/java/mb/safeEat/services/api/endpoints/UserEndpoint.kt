package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.User

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

sealed interface UserEndpoint {
    @GET
    fun findAll(): List<User>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): User

    @POST
    fun create(@Body user: User?): User

    @POST("/many")
    fun createMany(@Body users: List<User?>?): List<User>

    @PUT
    fun update(@Body user: User?): User

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): User
}
