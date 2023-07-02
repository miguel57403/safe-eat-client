package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.PaymentDto
import mb.safeEat.services.api.models.Payment
import retrofit2.http.*

sealed interface PaymentEndpoint {
    @GET("/payments")
    suspend fun findAll(): List<Payment>

    @GET("/payments/{id}")
    suspend fun findById(@Path("id") id: String): Payment

    @GET("/payments/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Payment>

    @POST("/payments")
    suspend fun create(@Body payment: PaymentDto): Payment

    @PUT("/payments")
    suspend fun update(@Body payment: PaymentDto): Payment

    @DELETE("/payments/{id}")
    suspend fun delete(@Path("id") id: String): Payment
}
