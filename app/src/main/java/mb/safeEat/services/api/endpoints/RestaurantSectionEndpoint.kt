package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.RestaurantSection

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

sealed interface RestaurantSectionEndpoint {
    @GET
    fun findAll(): Any

    @GET("/{id}")
    fun findById(@Path("id") id: String?): Any

    @POST
    fun create(@Body restaurantSection: RestaurantSection?): Any

    @POST("/many")
    fun createMany(@Body restaurantSections: List<RestaurantSection?>?): Any

    @PUT
    fun update(@Body restaurantSection: RestaurantSection?): Any

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?): Any
}
