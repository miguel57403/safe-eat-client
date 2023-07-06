package mb.safeEat.services.api.dto

import mb.safeEat.services.api.models.Address
import mb.safeEat.services.api.models.Delivery
import mb.safeEat.services.api.models.Item
import mb.safeEat.services.api.models.Payment

data class OrderDraftDto(
    val addresses: List<Address>? = null,
    val deliveries: List<Delivery>? = null,
    val payments: List<Payment>? = null,
    val items: List<Item>? = null,
    val subtotal: Double? = null,
    val quantity: Int? = null,
)
