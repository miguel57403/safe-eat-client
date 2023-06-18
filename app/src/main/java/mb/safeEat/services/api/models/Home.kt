package mb.safeEat.services.api.models

data class Home(
    val id: String? = null,
    val advertisements: List<Advertisement>? = null,
    val restaurantSections: List<RestaurantSection>? = null,
)
