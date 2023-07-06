package mb.safeEat.services.api.dto

import mb.safeEat.services.api.models.Address
import mb.safeEat.services.api.models.Delivery
import mb.safeEat.services.api.models.Item
import mb.safeEat.services.api.models.Payment

data class OrderDraftDto(
    val addresses: List<Address>,
    val deliveries: List<Delivery>,
    val payments: List<Payment>,
    val items: List<Item>,
    val subtotal: Double,
    val quantity: Int,
)
