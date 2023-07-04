package mb.safeEat.services.api.models

data class RestaurantSection(
    val id: String,
    val name: String? = null,
    val restaurants: List<Restaurant>? = null,
)
