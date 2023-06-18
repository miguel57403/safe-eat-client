package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Notification
import retrofit2.http.*


interface NotificationEndpoint {
    @GET
    fun findAll(): List<Notification>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Notification

    @POST
    fun create(@Body notification: Notification?): Notification

    @POST("/many")
    fun createMany(@Body notifications: List<Notification?>?): List<Notification>

    @PUT
    fun update(@Body notification: Notification?): Notification

    @PATCH("/{id}")
    fun view(@Path("id") id: String?): Notification

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}