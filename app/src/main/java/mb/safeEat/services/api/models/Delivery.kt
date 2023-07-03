package mb.safeEat.services.api.models

data class Delivery(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val minimumTime: String? = null,
    val maximumTime: String? = null,
    val isSelected: Boolean? = null,
)
