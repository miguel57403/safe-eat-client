package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.CategoryDto
import mb.safeEat.services.api.models.Category
import retrofit2.http.*

sealed interface CategoryEndpoint {
    @GET
    suspend fun findAll(): List<Category>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): Category

    @POST
    suspend fun create(@Body category: CategoryDto): Category

    @POST("/many")
    suspend fun createMany(@Body categories: List<CategoryDto>): List<Category>

    @PUT
    suspend fun update(@Body category: CategoryDto): Category

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String?)
}
