package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Notification
import retrofit2.http.*

interface NotificationEndpoint {
    @GET("/notifications")
    suspend fun findAll(): List<Notification>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Notification

    @GET("user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Notification>

    @GET("restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") userId: String): List<Notification>

    @PATCH("{id}")
    suspend fun view(@Path("id") id: String): Notification

    @DELETE("{id}")
    suspend fun delete(@Path("id") id: String)
}
