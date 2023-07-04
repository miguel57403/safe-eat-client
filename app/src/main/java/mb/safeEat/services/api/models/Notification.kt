package mb.safeEat.services.api.models

data class Notification(
    val id: String,
    val content: String? = null,
    val orderId: String? = null,
    val receiver: String? = null,
    val isViewed: Boolean? = null,
    val time: String? = null,
    val restaurant: Restaurant? = null,
    val client: User? = null,
)
