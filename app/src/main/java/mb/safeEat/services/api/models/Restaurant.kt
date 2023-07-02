package mb.safeEat.services.api.models

import java.text.DecimalFormat

data class Restaurant(
    val id: String? = null,
    val name: String? = null,
    val logo: String? = null,
    val cover: String? = null,
    val deliveries: List<Delivery>? = null,
    val products: List<Product>? = null,

    val notifications: List<Notification>? = null,
    val productSections: List<ProductSection>? = null,
    val advertisements: List<Advertisement>? = null,
    val ingredients: List<Ingredient>? = null,
    val orders: List<Order>? = null,
    val owner: User? = null,
) {
    fun formattedDeliveryPrice(unit: String): String {
        val price = deliveries?.get(0)?.price
        return if (price != null) {
            unit + priceFormatter.format(price)
        } else {
            "[N/A]"
        }
    }

    fun formattedDeliveryInterval(): String {
        // TODO: Refactor the API
        val minimumTime = deliveries?.get(0)?.startTime
        val maximumTime = deliveries?.get(0)?.endTime
        return if (minimumTime != null && maximumTime != null) {
            "$minimumTime - $maximumTime"
        } else {
            "[N/A]"
        }
    }
}

val priceFormatter = DecimalFormat("#.00")

// TODO: remove ignored properties?