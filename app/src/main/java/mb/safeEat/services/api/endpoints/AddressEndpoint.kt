package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Address
import retrofit2.http.*

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
