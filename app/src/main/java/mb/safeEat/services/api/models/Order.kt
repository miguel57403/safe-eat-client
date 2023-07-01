package mb.safeEat.services.api.models

data class Order(
    val id: String? = null,
    val status: String? = null,
    val time: String? = null,
    val subtotal: Double? = null,
    val total: Double? = null,
    val quantity: Int? = null,
    val address: Address? = null,
    val payment: Payment? = null,
    val delivery: Delivery? = null,
    val items: List<Item>? = null,

    val restaurant: Restaurant? = null,
    val feedback: Feedback? = null,
    val client: User? = null,
)

// TODO: remove ignored properties?