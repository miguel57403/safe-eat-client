package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.CategoryDto
import mb.safeEat.services.api.models.Category
import retrofit2.http.*

sealed interface CategoryEndpoint {
    @GET("/categories")
    suspend fun findAll(): List<Category>

    @GET("/categories/{id}")
    suspend fun findById(@Path("id") id: String): Category

    @POST("/categories")
    suspend fun create(@Body category: CategoryDto): Category

    @PUT("/categories")
    suspend fun update(@Body category: CategoryDto): Category

    // TODO: update the image file type
    @PUT("/categories/{id}/image")
    suspend fun updateImage(@Path("id") id: String, @Query("image") imageFile: String): Category

    @DELETE("/categories/{id}")
    suspend fun delete(@Path("id") id: String?)
}
