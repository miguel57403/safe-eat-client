package mb.safeEat.services.api.models

data class Restaurant(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val cover: String? = null,
//    val deliveries: List<Delivery>? = ArrayList<Delivery>(),
    val products: List<Product> = ArrayList(),
    val productSections: List<ProductSection> = ArrayList(),
//    val advertisements: List<Advertisement>? = ArrayList<Advertisement>(),
//    val ingredients: List<Ingredient>? = ArrayList<Ingredient>(),
//    val orders: List<Order>? = ArrayList(),
    val owner: User? = null,
)
