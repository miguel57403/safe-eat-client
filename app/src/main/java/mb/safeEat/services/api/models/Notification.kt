package mb.safeEat.services.api.models

// TODO: Update the model with the API
data class Notification(
    val id: String? = null,
    val content: String? = null,
    val time: String? = null,
    val isViewed: Boolean? = false,
    val order: Order? = null,
    val restaurant: Restaurant? = null
)
