package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.PaymentDto
import mb.safeEat.services.api.models.Payment
import retrofit2.http.*

sealed interface PaymentEndpoint {
    @GET
    suspend fun findAll(): List<Payment>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Payment

    @GET("/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Payment>

    @POST
    suspend fun create(
        @Body payment: PaymentDto,
        @Query("userId") userId: String,
    ): Payment

    @POST("/many")
    suspend fun createMany(
        @Body payments: List<PaymentDto>,
        @Query("userId") userId: String,
    ): List<Payment>

    @PUT
    suspend fun update(@Body payment: PaymentDto): Payment

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("userId") userId: String): Payment
}
