package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Cart
import retrofit2.http.*

sealed interface CartEndpoint {
    @GET
    suspend fun findAll(): List<Cart>

    @GET("/{id}")
    suspend fun findById(@Path("id") id:String): Cart

    @POST
    fun create(@Query("userId") userId: String?): Cart

    @PUT
    fun empty(@Query("cartId") cartId: String?): Cart

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}
