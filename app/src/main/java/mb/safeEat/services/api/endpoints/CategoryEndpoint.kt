package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.CategoryDto
import mb.safeEat.services.api.models.Category
import retrofit2.http.*

sealed interface CategoryEndpoint {
    @GET("/categories")
    suspend fun findAll(): List<Category>

    @GET("{id}")
    suspend fun findById(@Path("id") id: String): Category

    @POST
    suspend fun create(@Body category: CategoryDto): Category

    @PUT
    suspend fun update(@Body category: CategoryDto): Category

    // TODO: update the image file type
    @PUT("{id}/image")
    suspend fun updateImage(@Path("id") id: String, @Query("image") imageFile: String): Category

    @DELETE("{id}")
    suspend fun delete(@Path("id") id: String?)
}
