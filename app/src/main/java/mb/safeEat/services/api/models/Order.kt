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
    val feedbackId: String? = null,
    val restaurant: Restaurant? = null,
    val client: User? = null,
)
