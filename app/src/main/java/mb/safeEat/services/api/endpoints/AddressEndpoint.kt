package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.AddressDto
import mb.safeEat.services.api.models.Address
import retrofit2.http.*

sealed interface AddressEndpoint {
    @GET("/addresses")
    suspend fun findAll(): List<Address>

    @GET("/addresses/{id}")
    suspend fun findById(@Path("id") id: String): Address

    @GET("/addresses/user/{userId}")
    suspend fun findAllByUser(@Path("userId") userId: String): List<Address>

    @POST("/addresses")
    suspend fun create(@Body address: AddressDto): Address

    @PUT("/addresses")
    suspend fun update(@Body address: AddressDto): Address

    @DELETE("/addresses/{id}")
    suspend fun delete(@Path("id") id: String)
}
