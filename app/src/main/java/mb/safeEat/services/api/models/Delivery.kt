package mb.safeEat.services.api.models

import mb.safeEat.functions.formatPrice

data class Delivery(
    val id: String? = null,
    val name: String? = null,
    val price: Double? = null,
    val minimumTime: String? = null,
    val maximumTime: String? = null,
    val isSelected: Boolean? = null,
) {
    fun formattedDeliveryInterval(): String {
        return if (minimumTime != null && maximumTime != null) {
            "$minimumTime - $maximumTime"
        } else {
            "[N/A]"
        }
    }

    fun formattedDeliveryPrice(unit: String): String {
        return if (price != null) {
            formatPrice(unit, price)
        } else {
            "[N/A]"
        }
    }
}
