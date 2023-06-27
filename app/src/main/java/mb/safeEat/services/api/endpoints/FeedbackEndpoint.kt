package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.FeedbackDto
import mb.safeEat.services.api.models.Feedback
import retrofit2.http.*

sealed interface FeedbackEndpoint {
    @GET
    suspend fun findAll(): List<Feedback>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Feedback

    @POST
    suspend fun create(
        @Body feedback: FeedbackDto,
        @Query("orderId") orderId: String,
    ): Feedback

    @PUT
    suspend fun update(@Body feedback: FeedbackDto): Feedback

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("orderId") orderId: String?)
}
