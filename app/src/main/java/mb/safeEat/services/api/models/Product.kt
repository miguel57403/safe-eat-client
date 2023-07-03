package mb.safeEat.services.api.models

data class Product(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val image: String? = null,
    val isRestricted: Boolean? = null,
    val restaurantId: String? = null,
)
