package mb.safeEat.services.api.dto

data class DeliveryDto(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val startTime: String? = null,
    val endTime: String? = null,
)