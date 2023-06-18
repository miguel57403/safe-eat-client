package mb.safeEat.services.api.models

data class Product(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val images: List<String>? = null,
    val isRestricted: Boolean? = null,
//    val categories: List<Category>? = null,
//    val ingredients: List<Ingredient>? = null,
)
