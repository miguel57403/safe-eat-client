package mb.safeEat.services.api.models

data class Payment(
    val id: String? = null,
    val type: String? = null,
    val name: String? = null,
    val number: Int? = null,
    val expirationDate: String? = null,
    val cvv: Int? = null,
)
