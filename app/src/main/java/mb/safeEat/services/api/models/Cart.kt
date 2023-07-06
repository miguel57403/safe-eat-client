package mb.safeEat.services.api.models

data class Cart(
    val id: String,
    val quantity: Int,
    val subtotal: Double,
    val items: List<Item>,
)
