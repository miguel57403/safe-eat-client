package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Feedback
import retrofit2.http.*

sealed interface FeedbackEndpoint {
    @GET
    suspend fun findAll(): List<Feedback>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Feedback

    @POST
    fun create(
        @Body feedback: Feedback?,
        @Query("orderId") orderId: String?,
    ): Feedback

    @PUT
    fun update(@Body feedback: Feedback?): Feedback

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}
