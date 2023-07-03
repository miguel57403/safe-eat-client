package mb.safeEat.services.api.models

import mb.safeEat.functions.formatPrice

data class Restaurant(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val cover: String? = null,
    val deliveries: List<Delivery>? = null,
) {
    fun formattedDeliveryPrice(unit: String): String {
        val price = deliveries?.get(0)?.price
        return if (price != null) {
            formatPrice(unit, price)
        } else {
            "[N/A]"
        }
    }

    fun formattedDeliveryInterval(): String {
        val minimumTime = deliveries?.get(0)?.minimumTime
        val maximumTime = deliveries?.get(0)?.maximumTime
        return if (minimumTime != null && maximumTime != null) {
            "$minimumTime - $maximumTime"
        } else {
            "[N/A]"
        }
    }
}
