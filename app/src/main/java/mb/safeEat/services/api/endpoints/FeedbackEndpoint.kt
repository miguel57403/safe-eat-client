package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.FeedbackDto
import mb.safeEat.services.api.models.Feedback
import retrofit2.http.*

sealed interface FeedbackEndpoint {
    @GET("/feedbacks")
    suspend fun findAll(): List<Feedback>

    @GET("/feedbacks/{id}")
    suspend fun findById(@Path("id") id: String): Feedback

    @GET("/feedbacks/order/{orderId}")
    suspend fun findByOrder(@Path("orderId") orderId: String): Feedback

    @POST("/feedbacks/order/{orderId}")
    suspend fun create(
        @Body feedback: FeedbackDto,
        @Path("orderId") orderId: String,
    ): Feedback

    @PUT("/feedbacks")
    suspend fun update(@Body feedback: FeedbackDto): Feedback

    @DELETE("/feedbacks/{id}")
    suspend fun delete(@Path("id") id: String)
}
