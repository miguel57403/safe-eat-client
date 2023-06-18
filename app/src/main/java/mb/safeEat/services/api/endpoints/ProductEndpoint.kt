package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Product

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

sealed interface ProductEndpoint {
    @GET
    suspend fun findAll(): List<Product>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Product

    @POST
    suspend fun create(
        @Body product: Product?,
        @Query("restaurantId") restaurantId: String?
    ): Product

    @POST("/many")
    suspend fun createMany(
        @Body products: List<Product?>?,
        @Query("restaurantId") restaurantId: String?
    ): List<Product>

    @PUT
    suspend fun update(@Body product: Product?): Product

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String?)
}