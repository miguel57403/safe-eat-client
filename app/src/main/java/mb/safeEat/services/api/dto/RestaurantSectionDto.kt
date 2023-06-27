package mb.safeEat.services.api.dto

data class RestaurantSectionDto(
    val id: String? = null,
    val name: String? = null,
    val restaurantIds: List<String>? = null,
)
