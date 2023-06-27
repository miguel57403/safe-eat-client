package mb.safeEat.services.api.dto

data class OrderDto(
    val id: String? = null,
    val addressId: String? = null,
    val paymentId: String? = null,
    val deliveryId: String? = null,
    val itemIds: List<String>? = null,
    val restaurantId: String? = null,
    val clientId: String? = null,
)
