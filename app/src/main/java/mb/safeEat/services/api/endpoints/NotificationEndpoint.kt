package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Notification
import retrofit2.http.*

interface NotificationEndpoint {
    @GET("/notifications")
    suspend fun findAll(): List<Notification>

    @GET("/notifications/{id}")
    suspend fun findById(@Path("id") id: String): Notification

    @GET("/notifications/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Notification>

    @GET("/notifications/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") userId: String): List<Notification>

    @PATCH("/notifications/{id}")
    suspend fun view(@Path("id") id: String): Notification

    @DELETE("/notifications/{id}")
    suspend fun delete(@Path("id") id: String)
}
