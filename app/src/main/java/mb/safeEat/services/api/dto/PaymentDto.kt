package mb.safeEat.services.api.dto

data class PaymentDto(
    val id: String? = null,
    val type: String? = null,
    val name: String? = null,
    val number: Int? = null,
    val expirationDate: String? = null,
    val cvv: Int? = null,
)
