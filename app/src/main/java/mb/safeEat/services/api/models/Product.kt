package mb.safeEat.services.api.models

data class Product(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val image: String? = null,
    val isRestricted: Boolean? = null,

    val category: Category? = null,
    val ingredients: List<Ingredient>? = null,
)

// TODO: remove ignored properties?