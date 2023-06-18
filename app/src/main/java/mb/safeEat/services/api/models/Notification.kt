package mb.safeEat.services.api.models

data class Notification(
    val id: String? = null,
    val content: String? = null,
    val time: String? = null,
    val isViewed: Boolean? = false,
    val order: Order? = null,
)
