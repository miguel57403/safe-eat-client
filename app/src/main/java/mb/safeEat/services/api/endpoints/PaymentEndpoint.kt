package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Payment

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

sealed interface PaymentEndpoint{
    @GET
    fun findAll(): List<Payment>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Payment

    @POST
    fun create(
        @Body payment: Payment?,
        @Query("userId") userId: String?
    ): Payment

    @POST("/many")
    fun createMany(
        @Body payments: List<Payment?>?,
        @Query("userId") userId: String?
    ): List<Payment>

    @PUT
    fun update(@Body payment: Payment?): Payment

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Payment
}