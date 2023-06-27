package mb.safeEat.services.api.dto

data class ProductDto(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val image: String? = null,
    val categoryId: String? = null,
    val ingredientIds: List<String>? = null,
)
