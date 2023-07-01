package mb.safeEat.services.api.models

data class Restaurant(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val cover: String? = null,
    val deliveries: List<Delivery>? = null,
    val products: List<Product>? = null,

    val notifications: List<Notification>? = null,
    val productSections: List<ProductSection>? = null,
    val advertisements: List<Advertisement>? = null,
    val ingredients: List<Ingredient>? = null,
    val orders: List<Order>? = null,
    val owner: User? = null,
)

// TODO: remove ignored properties?