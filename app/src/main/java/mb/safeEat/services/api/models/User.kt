package mb.safeEat.services.api.models

data class User(
    val id: String? = null,
    val password: String? = null,
    val image: String? = null,
    val name: String? = null,
    val email: String? = null,
    val cellphone: String? = null,
//    val cart: Cart? = null,
    val restrictions: List<Restriction> = ArrayList(),
    val payments: List<Payment> = ArrayList(),
//    val address: List<Address> = ArrayList<Address>(),
//    val orders: List<Order> = ArrayList(),
//    val notifications: List<Notification> = ArrayList<Notification>(),
    val restaurants: List<Restaurant> = ArrayList(),
)
