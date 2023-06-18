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
    fun findAll(): List<Restaurant>

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Restaurant

    @GET("/owner/{id}")
    fun findByOwner(@Path("id") id: String?): List<Restaurant>

    @POST
    fun create(@Body restaurant: Restaurant?): Restaurant

    @POST("/many")
    fun createMany(@Body restaurants: List<Restaurant?>?): List<Restaurant>

    @PUT
    fun update(@Body restaurant: Restaurant?): Restaurant

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}