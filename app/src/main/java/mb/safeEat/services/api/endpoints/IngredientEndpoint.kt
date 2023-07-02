package mb.safeEat.services.api.endpoints

import mb.safeEat.services.api.dto.IngredientDto
import mb.safeEat.services.api.models.Ingredient
import retrofit2.http.*

sealed interface IngredientEndpoint {
    @GET("/ingredients")
    suspend fun findAll(): List<Ingredient>

    @GET("/ingredients/{id}")
    suspend fun findById(@Path("id") id: String): Ingredient

    @GET("/ingredients/restaurant/{restaurantId}")
    suspend fun findByAllRestaurant(@Path("restaurantId") restaurantId: String): List<Ingredient>

    @GET("/ingredients/product/{productId}")
    suspend fun findByAllProduct(@Path("productId") productId: String): List<Ingredient>

    @POST("/ingredients/restaurant/{restaurantId}")
    suspend fun create(
        @Body ingredient: IngredientDto,
        @Path("restaurantId") restaurantId: String,
    ): Ingredient

    @PUT("/ingredients")
    suspend fun update(@Body ingredient: IngredientDto): Ingredient

    @DELETE("/ingredients/{id}")
    suspend fun delete(@Path("id") id: String)
}
