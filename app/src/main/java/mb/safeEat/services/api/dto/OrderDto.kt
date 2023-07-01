package mb.safeEat.services.api.dto

data class OrderDto(
    val addressId: String? = null,
    val paymentId: String? = null,
    val deliveryId: String? = null,
)
