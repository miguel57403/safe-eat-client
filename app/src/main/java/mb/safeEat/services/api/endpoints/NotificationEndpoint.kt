package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.NotificationDto
import mb.safeEat.services.api.models.Notification
import retrofit2.http.*

interface NotificationEndpoint {
    @GET
    suspend fun findAll(): List<Notification>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Notification

    @GET("/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Notification>

    @POST
    suspend fun create(@Body notification: NotificationDto, @Query("userId") userId: String): Notification

    @POST("/many")
    suspend fun createMany(@Body notifications: List<NotificationDto>, @Query("userId") userId: String): List<Notification>

    @PUT
    suspend fun update(@Body notification: NotificationDto): Notification

    @PATCH("/{id}")
    suspend fun view(@Path("id") id: String): Notification

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("userId") userId: String)
}
