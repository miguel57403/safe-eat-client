package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Cart
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

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