package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.models.Category
import retrofit2.http.*

sealed interface CategoryEndpoint {
    @GET
    suspend fun findAll(): List<Category>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Category

    @POST
    fun create(@Body category: Category?): Category

    @POST("/many")
    fun createMany(@Body categories: List<Category?>?): List<Category>

    @PUT
    fun update(@Body category: Category?): Category

    @DELETE("/{id}")
    fun delete(@Path("id") id: String?)
}
