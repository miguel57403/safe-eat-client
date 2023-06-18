package mb.safeEat.services.api.models

import mb.safeEat.components.Restaurant
import java.time.LocalDateTime


data class Order(
    val id: String? = null,
    val status: String? = null,
    val time: LocalDateTime? = null,
    val subtotal: Double = 0.0,
    val total: Double = 0.0,
    val quantity: Int = 0,
    val address: Address? = null,
    //val payment: Payment = null,
    val delivery: Delivery? = null,
    val items: List<Item>? = null,
    val restaurant: Restaurant? = null,
    val feedback: Feedback? = null,
    //val client: User = null
)