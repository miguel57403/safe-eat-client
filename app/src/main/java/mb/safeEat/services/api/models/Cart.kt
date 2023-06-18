package mb.safeEat.services.api.models

data class Cart(
    val id: String? = null,
    val quantity: Int? = null,
    val subtotal: Double? = null,
    val items: List<Item>? = null,
)
