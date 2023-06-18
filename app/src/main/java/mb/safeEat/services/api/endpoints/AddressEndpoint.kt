package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Address
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query


sealed interface AddressEndpoint {
    @GET
    suspend fun findAll(): List<Address>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Address

    @POST
    fun create(
        @Body address: Address?,
        @Query("userId") userId: String?
    ): Address

    @POST("/many")
    fun createMany(
        @Body addresses: List<Address?>?,
        @Query("userId") userId: String?
    ): List<Address>

    @PUT
    fun update(@Body address: Address?): Address

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}
