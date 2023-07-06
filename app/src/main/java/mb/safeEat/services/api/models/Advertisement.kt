package mb.safeEat.services.api.models

data class Advertisement(
    val id: String,
    val title: String? = null,
    val image: String? = null,
    val restaurantId: String? = null,
)
