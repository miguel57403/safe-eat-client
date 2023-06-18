package mb.safeEat.services.api.models

data class User(
    val id: String? = null,
    val password: String? = null,
    val image: String? = null,
    val name: String? = null,
    val email: String? = null,
    val cellphone: String? = null,
    val cart: Cart? = null,
    val restrictions: List<Restriction>? = null,
    val payments: List<Payment>? = null,
    val address: List<Address>? = null,
    val orders: List<Order>? = null,
    val notifications: List<Notification>? = null,
    val restaurants: List<Restaurant>? = null,
)
