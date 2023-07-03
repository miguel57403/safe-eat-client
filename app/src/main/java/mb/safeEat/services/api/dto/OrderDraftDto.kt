package mb.safeEat.services.api.dto

import mb.safeEat.services.api.models.Address
import mb.safeEat.services.api.models.Delivery
import mb.safeEat.services.api.models.Payment

data class OrderDraftDto(
    val addresses: List<Address>? = null,
    val deliveries: List<Delivery>? = null,
    val payments: List<Payment>? = null,
    val subtotal: Double? = null,
    val quantity: Int? = null,
    // TODO: Assert on API
    val addressId: String? = null,
    val deliveryId: String? = null,
    val paymentId: String? = null,
)
