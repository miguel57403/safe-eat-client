package mb.safeEat.services.api.models

data class ProductSection (
    val id: String? = null,
    val name: String? = null,
    val products: List<Product>? = null,
)
