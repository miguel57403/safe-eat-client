package mb.safeEat.services.api.models

data class Item(
    val id: String,
    val product: Product? = null,
    val quantity: Int? = null,
    val subtotal: Double? = null
)


