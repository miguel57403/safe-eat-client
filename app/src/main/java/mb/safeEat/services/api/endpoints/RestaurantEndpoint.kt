package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Restaurant
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

sealed interface RestaurantEndpoint {
    @GET
    fun findAll(): Any

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Any

    @GET("/owner/{id}")
    fun findByOwner(@Path("id") id: String?): Any

    @POST
    fun create(@Body restaurant: Restaurant?): Any

    @POST("/many")
    fun createMany(@Body restaurants: List<Restaurant?>?): Any

    @PUT
    fun update(@Body restaurant: Restaurant?): Any

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Any
}