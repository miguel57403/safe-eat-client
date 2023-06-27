package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.ProductSectionDto
import mb.safeEat.services.api.models.ProductSection
import retrofit2.http.*

sealed interface ProductSectionEndpoint {
    @GET
    suspend fun findAll(): List<ProductSection>

    @GET("/{id}")
    suspend fun findById(@Path("id") id: String): ProductSection

    @GET("/restaurant/{restaurantId}")
    suspend fun findAllByRestaurant(@Path("restaurantId") restaurantId: String): List<ProductSection>

    @POST
    suspend fun create(
        @Body productSection: ProductSectionDto,
        @Query("restaurantId") restaurantId: String,
    ): ProductSection

    @POST("/many")
    suspend fun createMany(
        @Body productSections: List<ProductSectionDto>,
        @Query("restaurantId") restaurantId: String,
    ): List<ProductSection>

    @PUT
    suspend fun update(@Body productSection: ProductSectionDto): ProductSection

    @DELETE("/{id}")
    suspend fun delete(@Path("id") id: String, @Query("restaurantId") restaurantId: String?)
}
